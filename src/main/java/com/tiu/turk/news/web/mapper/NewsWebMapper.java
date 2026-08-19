package com.tiu.turk.news.web.mapper;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.common.dto.NewsSingleTranslationDto;
import com.tiu.turk.news.web.dto.news.NewsWebDto;
import com.tiu.turk.news.web.dto.news.NewsWebIndexDto;
import com.tiu.turk.news.web.mapper.NewsCategoryWebMapper;

public interface NewsWebMapper {
    public NewsWebIndexDto toDto(NewsSingleTranslationDto var1);

    public NewsWebDto toFullDto(NewsSingleTranslationDto var1);
}

