package com.tiu.turk.photogallery.admin.dto;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Generated;

public class PhotoGalleryDto {
    private Long id;
    private Map<String, PhotoGalleryTranslationDto> translations;
    private ImageDto previewImage;
    private List<ImageDto> images;
    private UserDto author;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Map<String, PhotoGalleryTranslationDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public ImageDto getPreviewImage() {
        return this.previewImage;
    }

    @Generated
    public List<ImageDto> getImages() {
        return this.images;
    }

    @Generated
    public UserDto getAuthor() {
        return this.author;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTranslations(Map<String, PhotoGalleryTranslationDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setPreviewImage(ImageDto previewImage) {
        this.previewImage = previewImage;
    }

    @Generated
    public void setImages(List<ImageDto> images) {
        this.images = images;
    }

    @Generated
    public void setAuthor(UserDto author) {
        this.author = author;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

