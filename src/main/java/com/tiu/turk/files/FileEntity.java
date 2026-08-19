package com.tiu.turk.files;

import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(name="files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="file_name", nullable=false)
    private String fileName;
    @Column(name="file_path", nullable=false, length=512)
    private String filePath;
    @Column(name="file_file_name", nullable=false, length=255)
    private String fileFileName;
    @Column(name="file_type", nullable=false)
    private String fileType;
    @Column(name="file_content_type")
    private String fileContentType;
    @Column(name="file_size")
    private Long fileSize;
    @Column(name="md5_hash", length=35)
    private String md5Hash;
    @Column(name="created_by_module")
    private String createdByModule;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id")
    private UserEntity author;
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getFileName() {
        return this.fileName;
    }

    @Generated
    public String getFilePath() {
        return this.filePath;
    }

    @Generated
    public String getFileFileName() {
        return this.fileFileName;
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
    public Long getFileSize() {
        return this.fileSize;
    }

    @Generated
    public String getMd5Hash() {
        return this.md5Hash;
    }

    @Generated
    public String getCreatedByModule() {
        return this.createdByModule;
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
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Generated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Generated
    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
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
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Generated
    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    @Generated
    public void setCreatedByModule(String createdByModule) {
        this.createdByModule = createdByModule;
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

