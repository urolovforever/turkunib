package com.tiu.turk.news.admin.mapper;

import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsCategoryTranslationMapper;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsCategoryTranslationMapperImpl
implements NewsCategoryTranslationMapper {
    public NewsTranslationEntity toEntity(NewsCategoryTranslationUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        NewsTranslationEntity newsTranslationEntity = new NewsTranslationEntity();
        newsTranslationEntity.setId(dto.getId());
        newsTranslationEntity.setTitle(dto.getTitle());
        newsTranslationEntity.setSlug(dto.getSlug());
        newsTranslationEntity.setDescription(dto.getDescription());
        return newsTranslationEntity;
    }

    public NewsCategoryTranslationDto toDto(NewsCategoryTranslationEntity entity) {
        if (entity == null) {
            return null;
        }
        NewsCategoryTranslationDto newsCategoryTranslationDto = new NewsCategoryTranslationDto();
        newsCategoryTranslationDto.setId(entity.getId());
        newsCategoryTranslationDto.setTitle(entity.getTitle());
        newsCategoryTranslationDto.setSlug(entity.getSlug());
        newsCategoryTranslationDto.setDescription(entity.getDescription());
        return newsCategoryTranslationDto;
    }
}

