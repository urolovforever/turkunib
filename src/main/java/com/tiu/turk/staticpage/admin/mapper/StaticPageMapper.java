package com.tiu.turk.staticpage.admin.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.admin.dto.StaticPageCreateDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageUpdateDto;
import com.tiu.turk.staticpage.admin.mapper.StaticPageTranslationMapper;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.EnumMap;
import java.util.Map;

public interface StaticPageMapper {
    public StaticPageDto toDto(StaticPageEntity var1);

    @Mapping(target="translations", expression="java(buildTranslations(dto))")
    public StaticPageEntity toEntity(StaticPageCreateDto var1);

    public StaticPageEntity toEntity(StaticPageUpdateDto var1);

    default public Map<TranslationLocale, StaticPageTranslationEntity> buildTranslations(StaticPageCreateDto dto) {
        EnumMap<TranslationLocale, StaticPageTranslationEntity> map = new EnumMap<TranslationLocale, StaticPageTranslationEntity>(TranslationLocale.class);
        if (dto == null || dto.getTranslations() == null) {
            return map;
        }
        for (Map.Entry<TranslationLocale, StaticPageTranslationDto> entry : dto.getTranslations().entrySet()) {
            TranslationLocale locale = entry.getKey();
            StaticPageTranslationDto translationDto = entry.getValue();
            if (locale == null || translationDto == null) {
                continue;
            }
            StaticPageTranslationEntity translation = new StaticPageTranslationEntity();
            translation.setTitle(translationDto.getTitle());
            translation.setContent(translationDto.getContent());
            translation.setLocale(locale);
            map.put(locale, translation);
        }
        return map;
    }
}

