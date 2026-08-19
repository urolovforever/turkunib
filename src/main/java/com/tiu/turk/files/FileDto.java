package com.tiu.turk.files;

import java.time.LocalDateTime;
import lombok.Generated;

public class FileDto {
    private Long id;
    private String url;
    private String fileType;
    private String fileContentType;
    private String fileName;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public String getFileType() {
        return this.fileType;
    }

    @Generated
    public String getFileContentType() {
        return this.fileContentType;
    }

    @Generated
    public String getFileName() {
        return this.fileName;
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
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Generated
    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    @Generated
    public void setFileName(String fileName) {
        this.fileName = fileName;
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

