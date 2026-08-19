package com.tiu.turk.news.admin.mapper;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.admin.dto.news.NewsCreateDto;
import com.tiu.turk.news.admin.dto.news.NewsDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationUpdateDto;
import com.tiu.turk.news.admin.dto.news.NewsUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsCategoryMapper;
import com.tiu.turk.news.admin.mapper.NewsMapper;
import com.tiu.turk.news.admin.mapper.NewsTranslationMapper;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsMapperImpl
implements NewsMapper {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private NewsCategoryMapper newsCategoryMapper;
    @Autowired
    private NewsTranslationMapper newsTranslationMapper;
    @Autowired
    private UserMapper userMapper;

    public NewsEntity toEntity(NewsCreateDto dto) {
        if (dto == null) {
            return null;
        }
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setCategory(this.newsCategoryMapper.fromId(dto.getCategoryId()));
        newsEntity.setAuthorName(dto.getAuthorName());
        newsEntity.setEnabled(Boolean.valueOf(dto.isEnabled()));
        newsEntity.setTranslations(this.stringNewsTranslationUpdateDtoMapToTranslationLocaleNewsTranslationEntityMap(dto.getTranslations()));
        return newsEntity;
    }

    public NewsEntity toEntity(NewsUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setCategory(this.newsCategoryMapper.fromId(dto.getCategoryId()));
        newsEntity.setId(dto.getId());
        newsEntity.setImage(this.imageDtoToImageEntity(dto.getImage()));
        newsEntity.setTranslations(this.stringNewsTranslationUpdateDtoMapToTranslationLocaleNewsTranslationEntityMap(dto.getTranslations()));
        newsEntity.setAuthorName(dto.getAuthorName());
        newsEntity.setEnabled(Boolean.valueOf(dto.isEnabled()));
        return newsEntity;
    }

    public NewsDto toDto(NewsEntity entity) {
        if (entity == null) {
            return null;
        }
        NewsDto newsDto = new NewsDto();
        newsDto.setId(entity.getId());
        newsDto.setCategory(this.newsCategoryMapper.toDto(entity.getCategory()));
        newsDto.setImage(this.imageMapper.toDto(entity.getImage()));
        newsDto.setAuthorName(entity.getAuthorName());
        newsDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        newsDto.setTranslations(this.translationLocaleNewsTranslationEntityMapToStringNewsTranslationDtoMap(entity.getTranslations()));
        if (entity.getEnabled() != null) {
            newsDto.setEnabled(entity.getEnabled().booleanValue());
        }
        newsDto.setUpdatedAt(entity.getUpdatedAt());
        newsDto.setCreatedAt(entity.getCreatedAt());
        return newsDto;
    }

    protected ImageEntity imageDtoToImageEntity(ImageDto imageDto) {
        if (imageDto == null) {
            return null;
        }
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(imageDto.getId());
        imageEntity.setImageName(imageDto.getImageName());
        imageEntity.setImageType(imageDto.getImageType());
        imageEntity.setImageContentType(imageDto.getImageContentType());
        return imageEntity;
    }

    protected Map<TranslationLocale, NewsTranslationEntity> stringNewsTranslationUpdateDtoMapToTranslationLocaleNewsTranslationEntityMap(Map<String, NewsTranslationUpdateDto> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<TranslationLocale, NewsTranslationEntity> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<String, NewsTranslationUpdateDto> entry : map.entrySet()) {
            TranslationLocale key = Enum.valueOf(TranslationLocale.class, entry.getKey());
            NewsTranslationEntity value = this.newsTranslationMapper.toEntity(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }

    protected Map<String, NewsTranslationDto> translationLocaleNewsTranslationEntityMapToStringNewsTranslationDtoMap(Map<TranslationLocale, NewsTranslationEntity> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<String, NewsTranslationDto> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<TranslationLocale, NewsTranslationEntity> entry : map.entrySet()) {
            String key = entry.getKey().name();
            NewsTranslationDto value = this.newsTranslationMapper.toDto(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }
}

