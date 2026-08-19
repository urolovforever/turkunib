package com.tiu.turk.staticpage.admin.dto;

import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Generated;

public class StaticPageDto {
    private Long id;
    private Map<String, StaticPageTranslationDto> translations;
    private String slug;
    private String content;
    private boolean enabled;
    private String type;
    private UserDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Map<String, StaticPageTranslationDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public String getSlug() {
        return this.slug;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public UserDto getAuthor() {
        return this.author;
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
    public void setTranslations(Map<String, StaticPageTranslationDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setAuthor(UserDto author) {
        this.author = author;
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

