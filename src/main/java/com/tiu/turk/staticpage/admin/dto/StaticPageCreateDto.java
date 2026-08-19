package com.tiu.turk.staticpage.admin.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.Generated;

public class StaticPageCreateDto {
    private String slug;
    @Valid
    private Map<TranslationLocale, StaticPageTranslationDto> translations;
    private boolean enabled;

    @Generated
    public String getSlug() {
        return this.slug;
    }

    @Generated
    public Map<TranslationLocale, StaticPageTranslationDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Generated
    public void setTranslations(Map<TranslationLocale, StaticPageTranslationDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

