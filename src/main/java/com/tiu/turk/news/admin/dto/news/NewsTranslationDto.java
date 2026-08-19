package com.tiu.turk.news.admin.dto.news;

import lombok.Generated;

public class NewsTranslationDto {
    private String title;
    private String slug;
    private String content;
    private String description;
    private String locale;

    @Generated
    public String getTitle() {
        return this.title;
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
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getLocale() {
        return this.locale;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
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
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setLocale(String locale) {
        this.locale = locale;
    }
}

