package com.tiu.turk.news.admin.dto.news;

import jakarta.validation.constraints.Size;
import lombok.Generated;

public class NewsTranslationUpdateDto {
    private Long id;
    @Size(max=512, message="Title length must not exceed 512 characters")
    private @Size(max=512, message="Title length must not exceed 512 characters") String title;
    private String content;
    @Size(max=1024, message="Description length must not exceed 1024 characters")
    private @Size(max=1024, message="Description length must not exceed 1024 characters") String description;
    private String slug;
    private String locale;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getSlug() {
        return this.slug;
    }

    @Generated
    public String getLocale() {
        return this.locale;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Generated
    public void setLocale(String locale) {
        this.locale = locale;
    }
}

