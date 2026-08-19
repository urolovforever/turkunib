package com.tiu.turk.banner.admin.dto;

import lombok.Generated;

public class BannerTranslationDto {
    private Long id;
    private String title;
    private String shortTitle;
    private String subTitle;
    private String urlTitle;
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
    public String getSubTitle() {
        return this.subTitle;
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
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

