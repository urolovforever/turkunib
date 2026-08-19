package com.tiu.turk.application.web.service;

import com.tiu.turk.application.common.ApplicantType;
import com.tiu.turk.application.common.ApplicationStatus;
import com.tiu.turk.application.common.Semester;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.application.common.repository.ApplicationRepository;
import com.tiu.turk.application.web.dto.StudentApplicationDto;
import com.tiu.turk.application.web.dto.TeacherApplicationDto;
import com.tiu.turk.files.FileService;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Orhun Exchange applications submitted from the student portal.
 * Validation errors are thrown as IllegalArgumentException whose message is an
 * i18n key, resolved by the controller for the current locale.
 */
@Service
public class ApplicationWebService {
    /** Statuses that DON'T block a re-application to the same round. */
    private static final List<ApplicationStatus> REJECTED_STATUSES =
            List.of(ApplicationStatus.HOME_REJECTED, ApplicationStatus.REJECTED);

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final FileService fileService;

    /** True when the round is open for applications (enabled + period started + deadline not passed; deadline day inclusive). */
    public static boolean isOpen(ScholarshipEntity scholarship) {
        LocalDate today = LocalDate.now();
        boolean started = scholarship.getApplicationStart() == null || !today.isBefore(scholarship.getApplicationStart());
        boolean notEnded = scholarship.getDeadline() == null || !scholarship.getDeadline().isBefore(today);
        return Boolean.TRUE.equals(scholarship.getEnabled()) && started && notEnded;
    }

    @Transactional
    public void createStudentApplication(StudentApplicationDto dto,
                                         MultipartFile transcript, MultipartFile languageCert, MultipartFile infoForm,
                                         Long authorId) throws IOException {
        requireText(dto.fullName());
        requireText(dto.studentId());
        requireText(dto.department());
        requireText(dto.phoneNumber());
        if (isMissing(transcript) || isMissing(languageCert) || isMissing(infoForm)) {
            throw new IllegalArgumentException("page.apply.error.files");
        }

        ApplicationEntity application = baseApplication(dto.fullName(), dto.homeUniversityId(), dto.hostUniversityId(),
                dto.department(), dto.phoneNumber(), authorId);
        applyRoundAndSemester(application, dto.scholarshipId(), dto.semester(), authorId);
        application.setApplicantType(ApplicantType.STUDENT);
        application.setStudentId(dto.studentId().trim());
        application.setTranscriptFile(this.fileService.store(transcript, transcript.getOriginalFilename(), "application", authorId));
        application.setLanguageCertFile(this.fileService.store(languageCert, languageCert.getOriginalFilename(), "application", authorId));
        application.setInfoFormFile(this.fileService.store(infoForm, infoForm.getOriginalFilename(), "application", authorId));
        this.applicationRepository.save(application);
    }

    @Transactional
    public void createTeacherApplication(TeacherApplicationDto dto, MultipartFile infoForm, Long authorId) throws IOException {
        requireText(dto.fullName());
        requireText(dto.department());
        requireText(dto.position());
        requireText(dto.phoneNumber());
        if (isMissing(infoForm)) {
            throw new IllegalArgumentException("page.apply.error.files");
        }

        ApplicationEntity application = baseApplication(dto.fullName(), dto.homeUniversityId(), dto.hostUniversityId(),
                dto.department(), dto.phoneNumber(), authorId);
        applyRoundAndSemester(application, dto.scholarshipId(), dto.semester(), authorId);
        application.setApplicantType(ApplicantType.TEACHER);
        application.setPosition(dto.position().trim());
        application.setInfoFormFile(this.fileService.store(infoForm, infoForm.getOriginalFilename(), "application", authorId));
        this.applicationRepository.save(application);
    }

    public List<ApplicationEntity> getStudentApplications(Long authorId) {
        return this.applicationRepository.findByAuthor_IdOrderByCreatedAtDesc(authorId);
    }

    private ApplicationEntity baseApplication(String fullName, Long homeUniversityId, Long hostUniversityId,
                                              String department, String phoneNumber, Long authorId) {
        if (homeUniversityId == null || hostUniversityId == null) {
            throw new IllegalArgumentException("page.apply.error.university");
        }
        if (homeUniversityId.equals(hostUniversityId)) {
            throw new IllegalArgumentException("page.apply.error.samehost");
        }
        MemberEntity home = this.memberRepository.findById(homeUniversityId)
                .filter(m -> Boolean.TRUE.equals(m.getEnabled()))
                .orElseThrow(() -> new IllegalArgumentException("page.apply.error.university"));
        MemberEntity host = this.memberRepository.findById(hostUniversityId)
                .filter(m -> Boolean.TRUE.equals(m.getEnabled()))
                .orElseThrow(() -> new IllegalArgumentException("page.apply.error.university"));

        ApplicationEntity application = new ApplicationEntity();
        application.setFullName(fullName.trim());
        application.setHomeUniversity(home);
        application.setHostUniversity(host);
        // Legacy column kept in sync so older admin filters/lists remain meaningful.
        application.setInstitution(home.getName());
        application.setDepartment(department.trim());
        application.setPhoneNumber(phoneNumber.trim());
        application.setAuthor(new UserEntity(authorId));
        application.setStatus(ApplicationStatus.SUBMITTED);
        return application;
    }

    /**
     * Ties the application to an OPEN Orhun round and a semester, and enforces
     * one application per user per round (a rejected one may be replaced).
     */
    private void applyRoundAndSemester(ApplicationEntity application, Long scholarshipId, Semester semester, Long authorId) {
        if (scholarshipId == null) {
            throw new IllegalArgumentException("page.apply.error.round");
        }
        if (semester == null) {
            throw new IllegalArgumentException("page.apply.error.semester");
        }
        ScholarshipEntity scholarship = this.scholarshipRepository.findById(scholarshipId)
                .orElseThrow(() -> new IllegalArgumentException("page.apply.error.round"));
        if (!isOpen(scholarship)) {
            throw new IllegalArgumentException("page.apply.error.deadline");
        }
        boolean alreadyApplied = this.applicationRepository
                .existsByAuthor_IdAndScholarship_IdAndStatusNotIn(authorId, scholarshipId, REJECTED_STATUSES);
        if (alreadyApplied) {
            throw new IllegalArgumentException("page.apply.error.duplicate");
        }
        application.setScholarship(scholarship);
        application.setSemester(semester);
    }

    private static void requireText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("page.apply.error.required");
        }
    }

    private static boolean isMissing(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    @Generated
    public ApplicationWebService(ApplicationRepository applicationRepository, MemberRepository memberRepository,
                                 ScholarshipRepository scholarshipRepository, FileService fileService) {
        this.applicationRepository = applicationRepository;
        this.memberRepository = memberRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.fileService = fileService;
    }
}
