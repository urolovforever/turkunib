package com.tiu.turk.banner.admin.dto;

import com.tiu.turk.banner.admin.dto.BannerTranslationDto;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Generated;

public class BannerDto {
    private Long id;
    private String url;
    private Map<String, BannerTranslationDto> translations;
    private Integer position;
    private ImageDto image;
    private boolean enabled;
    private UserDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public Map<String, BannerTranslationDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public Integer getPosition() {
        return this.position;
    }

    @Generated
    public ImageDto getImage() {
        return this.image;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
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
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setTranslations(Map<String, BannerTranslationDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setPosition(Integer position) {
        this.position = position;
    }

    @Generated
    public void setImage(ImageDto image) {
        this.image = image;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

