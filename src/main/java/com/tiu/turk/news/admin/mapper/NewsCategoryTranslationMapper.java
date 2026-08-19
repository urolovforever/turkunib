package com.tiu.turk.news.admin.mapper;

import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationUpdateDto;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;

public interface NewsCategoryTranslationMapper {
    public NewsTranslationEntity toEntity(NewsCategoryTranslationUpdateDto var1);

    public NewsCategoryTranslationDto toDto(NewsCategoryTranslationEntity var1);
}

