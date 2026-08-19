package com.tiu.turk.event.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.Generated;

public class EventTranslationUpdateDto {
    private Long id;
    @Size(max=512, message="Title length must not exceed 512 characters")
    private @Size(max=512, message="Title length must not exceed 512 characters") String title;
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

