package com.tiu.turk.application.common.entity;

import com.tiu.turk.application.common.ApplicantType;
import com.tiu.turk.application.common.ApplicationStatus;
import com.tiu.turk.application.common.Semester;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="applications")
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="full_name", length=512, nullable=false)
    private String fullName;
    @Column(name="student_id", length=512)
    private String studentId;
    @Column(name="institution", length=512, nullable=false)
    private String institution;
    @Column(name="department", length=512, nullable=false)
    private String department;
    @Column(name="phone_number", length=50, nullable=false)
    private String phoneNumber;
    @Enumerated(value=EnumType.STRING)
    @Column(name="applicant_type", nullable=false, length=20)
    private ApplicantType applicantType = ApplicantType.STUDENT;
    @Enumerated(value=EnumType.STRING)
    @Column(name="semester", length=20)
    private Semester semester;
    @Column(name="position", length=255)
    private String position;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="home_university_id")
    private MemberEntity homeUniversity;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="host_university_id")
    private MemberEntity hostUniversity;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="transcript_file_id")
    private FileEntity transcriptFile;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="language_cert_file_id")
    private FileEntity languageCertFile;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="info_form_file_id")
    private FileEntity infoFormFile;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id")
    private UserEntity author;
    @Enumerated(value=EnumType.STRING)
    @Column(name="status", nullable=false, length=100)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="matched_university_id")
    private MemberEntity matchedUniversity;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="acceptance_document_id")
    private FileEntity acceptanceDocument;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="scholarship_id")
    private ScholarshipEntity scholarship;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getFullName() {
        return this.fullName;
    }

    @Generated
    public String getStudentId() {
        return this.studentId;
    }

    @Generated
    public String getInstitution() {
        return this.institution;
    }

    @Generated
    public String getDepartment() {
        return this.department;
    }

    @Generated
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Generated
    public UserEntity getAuthor() {
        return this.author;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Generated
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Generated
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    @Generated
    public void setDepartment(String department) {
        this.department = department;
    }

    @Generated
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Generated
    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public ApplicationStatus getStatus() {
        return this.status;
    }

    @Generated
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    @Generated
    public MemberEntity getMatchedUniversity() {
        return this.matchedUniversity;
    }

    @Generated
    public void setMatchedUniversity(MemberEntity matchedUniversity) {
        this.matchedUniversity = matchedUniversity;
    }

    @Generated
    public FileEntity getAcceptanceDocument() {
        return this.acceptanceDocument;
    }

    @Generated
    public void setAcceptanceDocument(FileEntity acceptanceDocument) {
        this.acceptanceDocument = acceptanceDocument;
    }

    @Generated
    public ScholarshipEntity getScholarship() {
        return this.scholarship;
    }

    @Generated
    public void setScholarship(ScholarshipEntity scholarship) {
        this.scholarship = scholarship;
    }

    public ApplicantType getApplicantType() {
        return this.applicantType;
    }

    public Semester getSemester() {
        return this.semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public MemberEntity getHomeUniversity() {
        return this.homeUniversity;
    }

    public void setHomeUniversity(MemberEntity homeUniversity) {
        this.homeUniversity = homeUniversity;
    }

    public MemberEntity getHostUniversity() {
        return this.hostUniversity;
    }

    public void setHostUniversity(MemberEntity hostUniversity) {
        this.hostUniversity = hostUniversity;
    }

    public FileEntity getTranscriptFile() {
        return this.transcriptFile;
    }

    public void setTranscriptFile(FileEntity transcriptFile) {
        this.transcriptFile = transcriptFile;
    }

    public FileEntity getLanguageCertFile() {
        return this.languageCertFile;
    }

    public void setLanguageCertFile(FileEntity languageCertFile) {
        this.languageCertFile = languageCertFile;
    }

    public FileEntity getInfoFormFile() {
        return this.infoFormFile;
    }

    public void setInfoFormFile(FileEntity infoFormFile) {
        this.infoFormFile = infoFormFile;
    }
}

