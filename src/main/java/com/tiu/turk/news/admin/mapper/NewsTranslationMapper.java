package com.tiu.turk.news.admin.mapper;

import com.tiu.turk.news.admin.dto.news.NewsTranslationDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationUpdateDto;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;

public interface NewsTranslationMapper {
    public NewsTranslationEntity toEntity(NewsTranslationUpdateDto var1);

    public NewsTranslationDto toDto(NewsTranslationEntity var1);

    public NewsTranslationEntity fromId(Long var1);
}

