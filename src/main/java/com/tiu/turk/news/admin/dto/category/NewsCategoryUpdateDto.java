package com.tiu.turk.news.admin.dto.category;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Generated;

public class NewsCategoryUpdateDto {
    @NotNull(message="ID must not be null")
    private @NotNull(message="ID must not be null") Long id;
    @Valid
    private Map<String, NewsCategoryTranslationUpdateDto> translations;
    private ImageDto image;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Map<String, NewsCategoryTranslationUpdateDto> getTranslations() {
        return this.translations;
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
    public void setTranslations(Map<String, NewsCategoryTranslationUpdateDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setImage(ImageDto image) {
        this.image = image;
    }
}

