package com.tiu.turk.news.admin.dto.news;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.Generated;

public class NewsUpdateDto {
    @NotNull(message="ID must not be null")
    private @NotNull(message="ID must not be null") Long id;
    @NotNull(message="Category is required")
    private @NotNull(message="Category is required") Long categoryId;
    @Size(max=255, message="Author name length must not exceed 255 characters")
    private @Size(max=255, message="Author name length must not exceed 255 characters") String authorName;
    @Valid
    private Map<String, NewsTranslationUpdateDto> translations;
    private NewsCategoryDto category;
    private boolean enabled;
    private ImageDto image;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Long getCategoryId() {
        return this.categoryId;
    }

    @Generated
    public String getAuthorName() {
        return this.authorName;
    }

    @Generated
    public Map<String, NewsTranslationUpdateDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public NewsCategoryDto getCategory() {
        return this.category;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public ImageDto getImage() {
        return this.image;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Generated
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Generated
    public void setTranslations(Map<String, NewsTranslationUpdateDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setCategory(NewsCategoryDto category) {
        this.category = category;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setImage(ImageDto image) {
        this.image = image;
    }
}

