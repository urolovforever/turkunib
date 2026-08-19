package com.tiu.turk.banner.admin.mapper;

import com.tiu.turk.banner.admin.dto.BannerTranslationDto;
import com.tiu.turk.banner.admin.dto.BannerTranslationUpdateDto;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;

public interface BannerTranslationMapper {
    public BannerTranslationEntity toEntity(BannerTranslationUpdateDto var1);

    public BannerTranslationDto toDto(BannerTranslationEntity var1);
}

