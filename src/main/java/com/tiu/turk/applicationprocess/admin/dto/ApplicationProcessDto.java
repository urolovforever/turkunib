package com.tiu.turk.applicationprocess.admin.dto;

import com.tiu.turk.applicationprocess.common.ApplicationProcessStatus;
import com.tiu.turk.files.FileDto;
import com.tiu.turk.member.admin.dto.member.MemberDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import lombok.Generated;

public class ApplicationProcessDto {
    private Long id;
    private MemberDto universitySent;
    private MemberDto universityReceived;
    private FileDto file;
    private String comment;
    private ApplicationProcessStatus status;
    private UserDto author;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public MemberDto getUniversitySent() {
        return this.universitySent;
    }

    @Generated
    public MemberDto getUniversityReceived() {
        return this.universityReceived;
    }

    @Generated
    public FileDto getFile() {
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
    public UserDto getAuthor() {
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
    public void setUniversitySent(MemberDto universitySent) {
        this.universitySent = universitySent;
    }

    @Generated
    public void setUniversityReceived(MemberDto universityReceived) {
        this.universityReceived = universityReceived;
    }

    @Generated
    public void setFile(FileDto file) {
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
    public void setAuthor(UserDto author) {
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

