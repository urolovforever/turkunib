package com.tiu.turk.news.web.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.news.web.dto.category.NewsCategoryWebDto;
import com.tiu.turk.news.web.dto.category.NewsCategoryWebTranslationDto;

public interface NewsCategoryWebMapper {
    @Mapping(target="translation", expression="java(getFirstTranslation(entity))")
    public NewsCategoryWebDto toDto(NewsCategoryEntity var1);

    default public NewsCategoryWebTranslationDto getFirstTranslation(NewsCategoryEntity entity) {
        if (entity.getTranslations() != null && !entity.getTranslations().isEmpty()) {
            NewsCategoryTranslationEntity firstTranslation = (NewsCategoryTranslationEntity)entity.getTranslations().values().iterator().next();
            return new NewsCategoryWebTranslationDto(firstTranslation.getTitle(), firstTranslation.getSlug(), firstTranslation.getDescription());
        }
        return new NewsCategoryWebTranslationDto("", "", "");
    }
}

