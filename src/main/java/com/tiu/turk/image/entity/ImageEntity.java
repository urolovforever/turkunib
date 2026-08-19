package com.tiu.turk.image.entity;

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
@Table(name="images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="image_name", nullable=false)
    private String imageName;
    @Column(name="image_path", nullable=false, length=512)
    private String imagePath;
    @Column(name="image_file_name", nullable=false, length=255)
    private String imageFileName;
    @Column(name="image_type", nullable=false)
    private String imageType;
    @Column(name="image_content_type")
    private String imageContentType;
    @Column(name="image_size")
    private Long imageSize;
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
    public String getImageName() {
        return this.imageName;
    }

    @Generated
    public String getImagePath() {
        return this.imagePath;
    }

    @Generated
    public String getImageFileName() {
        return this.imageFileName;
    }

    @Generated
    public String getImageType() {
        return this.imageType;
    }

    @Generated
    public String getImageContentType() {
        return this.imageContentType;
    }

    @Generated
    public Long getImageSize() {
        return this.imageSize;
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
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Generated
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Generated
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Generated
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Generated
    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Generated
    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
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

