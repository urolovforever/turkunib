package com.tiu.turk.news.admin.dto.category;

import lombok.Generated;

public class NewsCategoryTranslationDto {
    private Long id;
    private String title;
    private String slug;
    private String description;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getSlug() {
        return this.slug;
    }

    @Generated
    public String getDescription() {
        return this.description;
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
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }
}

