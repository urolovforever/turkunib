package com.tiu.turk.banner.admin.dto;

import com.tiu.turk.banner.admin.dto.BannerTranslationUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.Generated;

public class BannerCreateDto {
    @Size(max=1024, message="URL length must not exceed 1024 characters")
    private @Size(max=1024, message="URL length must not exceed 1024 characters") String url;
    @Valid
    private Map<String, BannerTranslationUpdateDto> translations;
    private Integer position;
    private boolean enabled;

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
}
