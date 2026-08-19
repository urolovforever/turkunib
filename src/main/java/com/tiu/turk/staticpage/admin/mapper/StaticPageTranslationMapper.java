package com.tiu.turk.staticpage.admin.mapper;

import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;

public interface StaticPageTranslationMapper {
    public StaticPageTranslationEntity toEntity(StaticPageTranslationDto var1);

    public StaticPageTranslationDto toDto(StaticPageTranslationEntity var1);
}

