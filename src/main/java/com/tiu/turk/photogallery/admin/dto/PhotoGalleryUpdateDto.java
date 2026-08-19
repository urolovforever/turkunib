package com.tiu.turk.photogallery.admin.dto;

import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationUpdateDto;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.Generated;

public class PhotoGalleryUpdateDto {
    private Long id;
    @Valid
    private Map<String, PhotoGalleryTranslationUpdateDto> translations;
    private boolean enabled;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Map<String, PhotoGalleryTranslationUpdateDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTranslations(Map<String, PhotoGalleryTranslationUpdateDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

