package com.tiu.turk.news.admin.dto.news;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.admin.dto.news.NewsTranslationUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.Generated;

public class NewsCreateDto {
    @NotNull(message="Category is required")
    private @NotNull(message="Category is required") Long categoryId;
    @Valid
    private Map<String, NewsTranslationUpdateDto> translations;
    @Size(max=255, message="Author name length must not exceed 255 characters")
    private @Size(max=255, message="Author name length must not exceed 255 characters") String authorName;
    private TranslationLocale defaultLocale = TranslationLocale.EN;
    private boolean enabled;

    @Generated
    public Long getCategoryId() {
        return this.categoryId;
    }

    @Generated
    public Map<String, NewsTranslationUpdateDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public String getAuthorName() {
        return this.authorName;
    }

    @Generated
    public TranslationLocale getDefaultLocale() {
        return this.defaultLocale;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Generated
    public void setTranslations(Map<String, NewsTranslationUpdateDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Generated
    public void setDefaultLocale(TranslationLocale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
