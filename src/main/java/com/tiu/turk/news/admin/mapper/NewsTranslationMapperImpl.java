package com.tiu.turk.news.admin.mapper;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.admin.dto.news.NewsTranslationDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsTranslationMapper;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsTranslationMapperImpl
implements NewsTranslationMapper {
    public NewsTranslationEntity toEntity(NewsTranslationUpdateDto updateDto) {
        if (updateDto == null) {
            return null;
        }
        NewsTranslationEntity newsTranslationEntity = new NewsTranslationEntity();
        newsTranslationEntity.setId(updateDto.getId());
        if (updateDto.getLocale() != null) {
            newsTranslationEntity.setLocale(Enum.valueOf(TranslationLocale.class, updateDto.getLocale()));
        }
        newsTranslationEntity.setTitle(updateDto.getTitle());
        newsTranslationEntity.setSlug(updateDto.getSlug());
        newsTranslationEntity.setContent(updateDto.getContent());
        newsTranslationEntity.setDescription(updateDto.getDescription());
        return newsTranslationEntity;
    }

    public NewsTranslationDto toDto(NewsTranslationEntity entity) {
        if (entity == null) {
            return null;
        }
        NewsTranslationDto newsTranslationDto = new NewsTranslationDto();
        newsTranslationDto.setTitle(entity.getTitle());
        newsTranslationDto.setSlug(entity.getSlug());
        newsTranslationDto.setContent(entity.getContent());
        newsTranslationDto.setDescription(entity.getDescription());
        if (entity.getLocale() != null) {
            newsTranslationDto.setLocale(entity.getLocale().name());
        }
        return newsTranslationDto;
    }

    public NewsTranslationEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        NewsTranslationEntity newsTranslationEntity = new NewsTranslationEntity();
        newsTranslationEntity.setId(id);
        return newsTranslationEntity;
    }
}

