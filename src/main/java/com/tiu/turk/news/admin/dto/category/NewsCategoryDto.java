package com.tiu.turk.news.admin.dto.category;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Generated;

public class NewsCategoryDto {
    private Long id;
    private ImageDto image;
    private Map<String, NewsCategoryTranslationDto> translations;
    private UserDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NewsCategoryDto() {
    }

    public NewsCategoryDto(Long id) {
        this.id = id;
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public ImageDto getImage() {
        return this.image;
    }

    @Generated
    public Map<String, NewsCategoryTranslationDto> getTranslations() {
        return this.translations;
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
    public void setImage(ImageDto image) {
        this.image = image;
    }

    @Generated
    public void setTranslations(Map<String, NewsCategoryTranslationDto> translations) {
        this.translations = translations;
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

