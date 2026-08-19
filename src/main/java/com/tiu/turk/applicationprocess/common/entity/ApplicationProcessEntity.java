package com.tiu.turk.applicationprocess.common.entity;

import com.tiu.turk.applicationprocess.common.ApplicationProcessStatus;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.member.common.entity.MemberEntity;
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
@Table(name="applications_process")
public class ApplicationProcessEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="university_sent_id", nullable=false)
    private MemberEntity universitySent;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="university_received_id", nullable=false)
    private MemberEntity universityReceived;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="file_id", nullable=false)
    private FileEntity file;
    @Column(name="comment")
    private String comment;
    @Enumerated(value=EnumType.STRING)
    @Column(name="status", nullable=false)
    private ApplicationProcessStatus status = ApplicationProcessStatus.NEW;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id")
    private UserEntity author;
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
    public MemberEntity getUniversitySent() {
        return this.universitySent;
    }

    @Generated
    public MemberEntity getUniversityReceived() {
        return this.universityReceived;
    }

    @Generated
    public FileEntity getFile() {
        return this.file;
    }

    @Generated
    public String getComment() {
        return this.comment;
    }

    @Generated
    public ApplicationProcessStatus getStatus() {
        return this.status;
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
    public void setUniversitySent(MemberEntity universitySent) {
        this.universitySent = universitySent;
    }

    @Generated
    public void setUniversityReceived(MemberEntity universityReceived) {
        this.universityReceived = universityReceived;
    }

    @Generated
    public void setFile(FileEntity file) {
        this.file = file;
    }

    @Generated
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Generated
    public void setStatus(ApplicationProcessStatus status) {
        this.status = status;
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
}

