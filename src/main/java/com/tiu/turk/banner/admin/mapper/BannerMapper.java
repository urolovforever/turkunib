package com.tiu.turk.banner.admin.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.banner.admin.dto.BannerCreateDto;
import com.tiu.turk.banner.admin.dto.BannerDto;
import com.tiu.turk.banner.admin.dto.BannerTranslationUpdateDto;
import com.tiu.turk.banner.admin.dto.BannerUpdateDto;
import com.tiu.turk.banner.admin.mapper.BannerTranslationMapper;
import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.EnumMap;
import java.util.Map;

public interface BannerMapper {
    @Mapping(target="translations", expression="java(buildTranslations(createDto))")
    public BannerEntity toEntity(BannerCreateDto var1);

    public BannerEntity toEntity(BannerUpdateDto var1);

    public BannerDto toDto(BannerEntity var1);

    default public Map<TranslationLocale, BannerTranslationEntity> buildTranslations(BannerCreateDto dto) {
        EnumMap<TranslationLocale, BannerTranslationEntity> map = new EnumMap<TranslationLocale, BannerTranslationEntity>(TranslationLocale.class);
        if (dto == null || dto.getTranslations() == null) {
            return map;
        }
        for (Map.Entry<String, BannerTranslationUpdateDto> entry : dto.getTranslations().entrySet()) {
            BannerTranslationUpdateDto translationDto = entry.getValue();
            if (translationDto == null) {
                continue;
            }
            TranslationLocale locale = Enum.valueOf(TranslationLocale.class, entry.getKey());
            BannerTranslationEntity translation = new BannerTranslationEntity();
            translation.setTitle(translationDto.getTitle());
            translation.setShortTitle(translationDto.getShortTitle());
            translation.setDescription(translationDto.getDescription());
            translation.setUrlTitle(translationDto.getUrlTitle());
            translation.setLocale(locale);
            map.put(locale, translation);
        }
        return map;
    }
}

