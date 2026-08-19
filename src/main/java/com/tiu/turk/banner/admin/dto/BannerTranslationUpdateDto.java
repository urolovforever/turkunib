package com.tiu.turk.banner.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.Generated;

public class BannerTranslationUpdateDto {
    private Long id;
    @Size(max=512, message="Title length must not exceed 512 characters")
    private @Size(max=512, message="Title length must not exceed 512 characters") String title;
    @Size(max=256, message="Short title length must not exceed 256 characters")
    private @Size(max=256, message="Short title length must not exceed 256 characters") String shortTitle;
    @Size(max=256, message="URL title length must not exceed 256 characters")
    private @Size(max=256, message="URL title length must not exceed 256 characters") String urlTitle;
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
    public String getShortTitle() {
        return this.shortTitle;
    }

    @Generated
    public String getUrlTitle() {
        return this.urlTitle;
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
    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    @Generated
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }
}

