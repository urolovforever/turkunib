package com.tiu.turk.staticpage.admin.mapper;

import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import com.tiu.turk.staticpage.admin.mapper.StaticPageTranslationMapper;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import org.springframework.stereotype.Component;

@Component
public class StaticPageTranslationMapperImpl
implements StaticPageTranslationMapper {
    public StaticPageTranslationEntity toEntity(StaticPageTranslationDto dto) {
        if (dto == null) {
            return null;
        }
        StaticPageTranslationEntity staticPageTranslationEntity = new StaticPageTranslationEntity();
        staticPageTranslationEntity.setId(dto.getId());
        staticPageTranslationEntity.setTitle(dto.getTitle());
        staticPageTranslationEntity.setContent(dto.getContent());
        return staticPageTranslationEntity;
    }

    public StaticPageTranslationDto toDto(StaticPageTranslationEntity entity) {
        if (entity == null) {
            return null;
        }
        StaticPageTranslationDto staticPageTranslationDto = new StaticPageTranslationDto();
        staticPageTranslationDto.setId(entity.getId());
        staticPageTranslationDto.setTitle(entity.getTitle());
        staticPageTranslationDto.setContent(entity.getContent());
        return staticPageTranslationDto;
    }
}

