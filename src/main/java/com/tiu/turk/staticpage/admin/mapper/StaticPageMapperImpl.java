package com.tiu.turk.staticpage.admin.mapper;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.admin.dto.StaticPageCreateDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageUpdateDto;
import com.tiu.turk.staticpage.admin.mapper.StaticPageMapper;
import com.tiu.turk.staticpage.admin.mapper.StaticPageTranslationMapper;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaticPageMapperImpl
implements StaticPageMapper {
    @Autowired
    private StaticPageTranslationMapper staticPageTranslationMapper;
    @Autowired
    private UserMapper userMapper;

    public StaticPageDto toDto(StaticPageEntity entity) {
        if (entity == null) {
            return null;
        }
        StaticPageDto staticPageDto = new StaticPageDto();
        staticPageDto.setId(entity.getId());
        staticPageDto.setTranslations(this.translationLocaleStaticPageTranslationEntityMapToStringStaticPageTranslationDtoMap(entity.getTranslations()));
        staticPageDto.setSlug(entity.getSlug());
        if (entity.getEnabled() != null) {
            staticPageDto.setEnabled(entity.getEnabled().booleanValue());
        }
        if (entity.getType() != null) {
            staticPageDto.setType(entity.getType().name());
        }
        staticPageDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        staticPageDto.setCreatedAt(entity.getCreatedAt());
        staticPageDto.setUpdatedAt(entity.getUpdatedAt());
        return staticPageDto;
    }

    public StaticPageEntity toEntity(StaticPageCreateDto dto) {
        if (dto == null) {
            return null;
        }
        StaticPageEntity staticPageEntity = new StaticPageEntity();
        staticPageEntity.setSlug(dto.getSlug());
        staticPageEntity.setEnabled(Boolean.valueOf(dto.isEnabled()));
        staticPageEntity.setTranslations(this.buildTranslations(dto));
        return staticPageEntity;
    }

    public StaticPageEntity toEntity(StaticPageUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        StaticPageEntity staticPageEntity = new StaticPageEntity();
        staticPageEntity.setId(dto.getId());
        staticPageEntity.setTranslations(this.translationLocaleStaticPageTranslationDtoMapToTranslationLocaleStaticPageTranslationEntityMap(dto.getTranslations()));
        staticPageEntity.setSlug(dto.getSlug());
        staticPageEntity.setEnabled(Boolean.valueOf(dto.isEnabled()));
        return staticPageEntity;
    }

    protected Map<String, StaticPageTranslationDto> translationLocaleStaticPageTranslationEntityMapToStringStaticPageTranslationDtoMap(Map<TranslationLocale, StaticPageTranslationEntity> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<String, StaticPageTranslationDto> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<TranslationLocale, StaticPageTranslationEntity> entry : map.entrySet()) {
            String key = entry.getKey().name();
            StaticPageTranslationDto value = this.staticPageTranslationMapper.toDto(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }

    protected Map<TranslationLocale, StaticPageTranslationEntity> translationLocaleStaticPageTranslationDtoMapToTranslationLocaleStaticPageTranslationEntityMap(Map<TranslationLocale, StaticPageTranslationDto> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<TranslationLocale, StaticPageTranslationEntity> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<TranslationLocale, StaticPageTranslationDto> entry : map.entrySet()) {
            TranslationLocale key = entry.getKey();
            StaticPageTranslationEntity value = this.staticPageTranslationMapper.toEntity(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }
}

