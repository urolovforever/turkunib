package com.tiu.turk.image.dto;

import lombok.Generated;

public class ImageDto {
    private Long id;
    private String url;
    private String imageType;
    private String imageContentType;
    private String imageName;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public String getImageType() {
        return this.imageType;
    }

    @Generated
    public String getImageContentType() {
        return this.imageContentType;
    }

    @Generated
    public String getImageName() {
        return this.imageName;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Generated
    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Generated
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

