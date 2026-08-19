package com.tiu.turk.news.admin.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.admin.dto.category.NewsCategoryCreateDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsCategoryTranslationMapper;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.EnumMap;
import java.util.Map;

public interface NewsCategoryMapper {
    @Mapping(target="translations", expression="java(buildTranslations(dto))")
    public NewsCategoryEntity toEntity(NewsCategoryCreateDto var1);

    public NewsCategoryEntity toEntity(NewsCategoryUpdateDto var1);

    public NewsCategoryDto toDto(NewsCategoryEntity var1);

    public NewsCategoryEntity fromId(Long var1);

    default public Map<TranslationLocale, NewsCategoryTranslationEntity> buildTranslations(NewsCategoryCreateDto dto) {
        EnumMap<TranslationLocale, NewsCategoryTranslationEntity> map = new EnumMap<TranslationLocale, NewsCategoryTranslationEntity>(TranslationLocale.class);
        if (dto == null) {
            return map;
        }
        NewsCategoryTranslationEntity translation = new NewsCategoryTranslationEntity();
        translation.setTitle(dto.getTitle());
        translation.setDescription(dto.getDescription());
        translation.setSlug(dto.getSlug());
        translation.setLocale(TranslationLocale.EN);
        map.put(TranslationLocale.EN, translation);
        return map;
    }
}

