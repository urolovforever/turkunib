package com.tiu.turk.applicationprocess.admin.dto;

import lombok.Generated;

public class CreateApplicationDto {
    private Long universitySentId;
    private Long universityReceivedId;
    private String comment;

    @Generated
    public Long getUniversitySentId() {
        return this.universitySentId;
    }

    @Generated
    public Long getUniversityReceivedId() {
        return this.universityReceivedId;
    }

    @Generated
    public String getComment() {
        return this.comment;
    }

    @Generated
    public void setUniversitySentId(Long universitySentId) {
        this.universitySentId = universitySentId;
    }

    @Generated
    public void setUniversityReceivedId(Long universityReceivedId) {
        this.universityReceivedId = universityReceivedId;
    }

    @Generated
    public void setComment(String comment) {
        this.comment = comment;
    }
}

