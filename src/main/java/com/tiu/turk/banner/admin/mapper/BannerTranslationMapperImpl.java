package com.tiu.turk.banner.admin.mapper;

import com.tiu.turk.banner.admin.dto.BannerTranslationDto;
import com.tiu.turk.banner.admin.dto.BannerTranslationUpdateDto;
import com.tiu.turk.banner.admin.mapper.BannerTranslationMapper;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import org.springframework.stereotype.Component;

@Component
public class BannerTranslationMapperImpl
implements BannerTranslationMapper {
    public BannerTranslationEntity toEntity(BannerTranslationUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        BannerTranslationEntity bannerTranslationEntity = new BannerTranslationEntity();
        bannerTranslationEntity.setId(dto.getId());
        bannerTranslationEntity.setTitle(dto.getTitle());
        bannerTranslationEntity.setUrlTitle(dto.getUrlTitle());
        bannerTranslationEntity.setShortTitle(dto.getShortTitle());
        bannerTranslationEntity.setDescription(dto.getDescription());
        return bannerTranslationEntity;
    }

    public BannerTranslationDto toDto(BannerTranslationEntity entity) {
        if (entity == null) {
            return null;
        }
        BannerTranslationDto bannerTranslationDto = new BannerTranslationDto();
        bannerTranslationDto.setId(entity.getId());
        bannerTranslationDto.setTitle(entity.getTitle());
        bannerTranslationDto.setShortTitle(entity.getShortTitle());
        bannerTranslationDto.setUrlTitle(entity.getUrlTitle());
        bannerTranslationDto.setDescription(entity.getDescription());
        return bannerTranslationDto;
    }
}

