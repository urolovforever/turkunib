package com.tiu.turk.news.admin.dto.category;

import com.tiu.turk.common.enums.TranslationLocale;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class NewsCategoryCreateDto {
    private Long id;
    @NotBlank(message="Title must not be blank")
    @Size(max=512, message="Title length must not exceed 512 characters")
    private @NotBlank(message="Title must not be blank") @Size(max=512, message="Title length must not exceed 512 characters") String title;
    @Size(max=512, message="Slug length must not exceed 512 characters")
    private @Size(max=512, message="Slug length must not exceed 512 characters") String slug;
    private String description;
    private TranslationLocale defaultLocale = TranslationLocale.EN;

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
    public TranslationLocale getDefaultLocale() {
        return this.defaultLocale;
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

    @Generated
    public void setDefaultLocale(TranslationLocale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
}

