package com.tiu.turk.application.admin.service;

import com.tiu.turk.application.common.ApplicationDecision;
import com.tiu.turk.application.common.ApplicationStatus;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.application.common.repository.ApplicationRepository;
import com.tiu.turk.common.exception.PageNotFoundException;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileService;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.user.common.security.AppUserDetails;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    /** Statuses at which an application is visible to the HOST university. */
    public static final List<ApplicationStatus> HOST_VISIBLE_STATUSES =
            List.of(ApplicationStatus.HOME_APPROVED, ApplicationStatus.ACCEPTED, ApplicationStatus.REJECTED);

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    public Page<ApplicationEntity> getAllApplications(int page, int size) {
        return this.applicationRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Page<ApplicationEntity> getApplicationsByMemberName(String memberName, int page, int size) {
        return this.applicationRepository.findByMemberName(memberName, PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public List<ApplicationEntity> getOutgoingForMember(Long memberId, String memberName) {
        return this.applicationRepository.findOutgoingForMember(memberId, memberName == null ? "" : memberName);
    }

    public List<ApplicationEntity> getIncomingForMember(Long memberId) {
        return this.applicationRepository.findIncomingForMember(memberId, HOST_VISIBLE_STATUSES);
    }

    public List<ApplicationEntity> getAllForStatistics() {
        return this.applicationRepository.findAllForStatistics();
    }

    public ApplicationEntity getApplicationById(Long applicationId) {
        return this.applicationRepository.findById(applicationId).orElseThrow(() -> new PageNotFoundException("Application not found with ID: " + applicationId));
    }

    /** True when this admin acts for the application's HOME university (or is a full admin). */
    public boolean canHomeReview(ApplicationEntity app, AppUserDetails admin) {
        boolean pending = app.getStatus() == ApplicationStatus.SUBMITTED || app.getStatus() == ApplicationStatus.UNDER_REVIEW;
        return pending && app.getHomeUniversity() != null
                && (!admin.isUniversityMember() || app.getHomeUniversity().getId().equals(admin.getMemberId()));
    }

    /** True when this admin acts for the application's HOST university (or is a full admin). */
    public boolean canHostDecide(ApplicationEntity app, AppUserDetails admin) {
        return app.getStatus() == ApplicationStatus.HOME_APPROVED && app.getHostUniversity() != null
                && (!admin.isUniversityMember() || app.getHostUniversity().getId().equals(admin.getMemberId()));
    }

    /** A member admin may open only applications belonging to their university (either side). */
    public boolean canView(ApplicationEntity app, AppUserDetails admin) {
        if (!admin.isUniversityMember()) {
            return true;
        }
        Long memberId = admin.getMemberId();
        if (memberId == null) {
            return false;
        }
        boolean isHome = app.getHomeUniversity() != null && memberId.equals(app.getHomeUniversity().getId());
        boolean isHost = app.getHostUniversity() != null && memberId.equals(app.getHostUniversity().getId())
                && HOST_VISIBLE_STATUSES.contains(app.getStatus());
        boolean legacyByName = app.getHomeUniversity() == null && admin.getMemberName() != null
                && app.getInstitution() != null
                && app.getInstitution().toLowerCase().contains(admin.getMemberName().toLowerCase());
        return isHome || isHost || legacyByName;
    }

    /**
     * Two-stage Orhun Exchange review. Permission and state checks run server-side:
     * HOME_APPROVE/HOME_REJECT only by the home university (on SUBMITTED/UNDER_REVIEW),
     * ACCEPT/REJECT only by the host university (on HOME_APPROVED). Full admins may do both.
     */
    @Transactional
    public void decide(Long applicationId, ApplicationDecision decision, MultipartFile acceptanceFile,
                       AppUserDetails admin) throws IOException {
        ApplicationEntity app = getApplicationById(applicationId);
        switch (decision) {
            case HOME_APPROVE, HOME_REJECT -> {
                if (!canHomeReview(app, admin)) {
                    throw new IllegalStateException("Only the home university can review this application at its current stage.");
                }
                app.setStatus(decision == ApplicationDecision.HOME_APPROVE
                        ? ApplicationStatus.HOME_APPROVED : ApplicationStatus.HOME_REJECTED);
            }
            case ACCEPT, REJECT -> {
                if (!canHostDecide(app, admin)) {
                    throw new IllegalStateException("Only the host university can decide on this application after home approval.");
                }
                if (decision == ApplicationDecision.ACCEPT) {
                    if (acceptanceFile != null && !acceptanceFile.isEmpty()) {
                        FileEntity stored = this.fileService.store(acceptanceFile, acceptanceFile.getOriginalFilename(), "application", admin.getId());
                        app.setAcceptanceDocument(stored);
                    }
                    app.setStatus(ApplicationStatus.ACCEPTED);
                } else {
                    app.setStatus(ApplicationStatus.REJECTED);
                }
            }
        }
        this.applicationRepository.save(app);
    }

    @Transactional
    public void manageApplication(Long applicationId, ApplicationStatus status, Long matchedUniversityId, MultipartFile file, Long authorId) throws IOException {
        ApplicationEntity application = this.getApplicationById(applicationId);

        if (status != null) {
            application.setStatus(status);
        }

        if (matchedUniversityId != null && matchedUniversityId > 0L) {
            MemberEntity member = this.memberRepository.findById(matchedUniversityId)
                    .orElseThrow(() -> new PageNotFoundException("University not found with ID: " + matchedUniversityId));
            application.setMatchedUniversity(member);
        }

        if (file != null && !file.isEmpty()) {
            FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "application", authorId);
            application.setAcceptanceDocument(stored);
        }

        this.applicationRepository.save(application);
    }
}
