package com.tiu.turk.news.admin.dto.news;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Generated;

public class NewsDto {
    private Long id;
    private NewsCategoryDto category;
    private ImageDto image;
    private String authorName;
    private UserDto author;
    private Map<String, NewsTranslationDto> translations;
    private boolean enabled;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public NewsCategoryDto getCategory() {
        return this.category;
    }

    @Generated
    public ImageDto getImage() {
        return this.image;
    }

    @Generated
    public String getAuthorName() {
        return this.authorName;
    }

    @Generated
    public UserDto getAuthor() {
        return this.author;
    }

    @Generated
    public Map<String, NewsTranslationDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setCategory(NewsCategoryDto category) {
        this.category = category;
    }

    @Generated
    public void setImage(ImageDto image) {
        this.image = image;
    }

    @Generated
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Generated
    public void setAuthor(UserDto author) {
        this.author = author;
    }

    @Generated
    public void setTranslations(Map<String, NewsTranslationDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

