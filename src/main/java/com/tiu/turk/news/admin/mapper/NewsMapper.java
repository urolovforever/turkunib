package com.tiu.turk.news.admin.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.admin.dto.news.NewsCreateDto;
import com.tiu.turk.news.admin.dto.news.NewsDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationUpdateDto;
import com.tiu.turk.news.admin.dto.news.NewsUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsCategoryMapper;
import com.tiu.turk.news.admin.mapper.NewsTranslationMapper;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.EnumMap;
import java.util.Map;

public interface NewsMapper {
    @Mappings(value={@Mapping(source="categoryId", target="category"), @Mapping(target="translations", expression="java(buildTranslations(dto))")})
    public NewsEntity toEntity(NewsCreateDto var1);

    @Mapping(source="categoryId", target="category")
    public NewsEntity toEntity(NewsUpdateDto var1);

    public NewsDto toDto(NewsEntity var1);

    default public Map<TranslationLocale, NewsTranslationEntity> buildTranslations(NewsCreateDto dto) {
        EnumMap<TranslationLocale, NewsTranslationEntity> map = new EnumMap<TranslationLocale, NewsTranslationEntity>(TranslationLocale.class);
        if (dto == null || dto.getTranslations() == null) {
            return map;
        }
        for (Map.Entry<String, NewsTranslationUpdateDto> entry : dto.getTranslations().entrySet()) {
            NewsTranslationUpdateDto source = entry.getValue();
            if (source == null) {
                continue;
            }
            TranslationLocale locale = Enum.valueOf(TranslationLocale.class, entry.getKey());
            NewsTranslationEntity translation = new NewsTranslationEntity();
            translation.setTitle(source.getTitle());
            translation.setContent(source.getContent());
            translation.setDescription(source.getDescription());
            translation.setLocale(locale);
            map.put(locale, translation);
        }
        return map;
    }
}

