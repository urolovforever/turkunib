package com.tiu.turk.staticpage.admin.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import java.util.Map;
import lombok.Generated;

public class StaticPageUpdateDto {
    private Long id;
    private String slug;
    private Map<TranslationLocale, StaticPageTranslationDto> translations;
    private boolean enabled;

    @Generated
    public Long getId() {
        return this.id;
    }

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
    public void setId(Long id) {
        this.id = id;
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

