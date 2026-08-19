package com.tiu.turk.banner.admin.dto;

import com.tiu.turk.banner.admin.dto.BannerTranslationUpdateDto;
import com.tiu.turk.image.dto.ImageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.Generated;

public class BannerUpdateDto {
    @NotNull(message="ID must not be blank")
    private @NotNull(message="ID must not be blank") Long id;
    @Size(max=1024, message="URL length must not exceed 1024 characters")
    private @Size(max=1024, message="URL length must not exceed 1024 characters") String url;
    @Valid
    private Map<String, BannerTranslationUpdateDto> translations;
    private Integer position;
    private boolean enabled;
    private ImageDto image;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public Map<String, BannerTranslationUpdateDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public Integer getPosition() {
        return this.position;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public ImageDto getImage() {
        return this.image;
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
    public void setTranslations(Map<String, BannerTranslationUpdateDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setPosition(Integer position) {
        this.position = position;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setImage(ImageDto image) {
        this.image = image;
    }
}

