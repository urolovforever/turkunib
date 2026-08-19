package com.tiu.turk.news.web.mapper;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.common.dto.NewsSingleTranslationDto;
import com.tiu.turk.news.web.dto.category.NewsCategoryWebDto;
import com.tiu.turk.news.web.dto.news.NewsWebDto;
import com.tiu.turk.news.web.dto.news.NewsWebIndexDto;
import com.tiu.turk.news.web.mapper.NewsCategoryWebMapper;
import com.tiu.turk.news.web.mapper.NewsWebMapper;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsWebMapperImpl
implements NewsWebMapper {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private NewsCategoryWebMapper newsCategoryWebMapper;

    public NewsWebIndexDto toDto(NewsSingleTranslationDto entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        TranslationLocale locale = null;
        String title = null;
        String slug = null;
        String content = null;
        String description = null;
        ImageDto image = null;
        NewsCategoryWebDto category = null;
        String authorName = null;
        LocalDateTime createdAt = null;
        id = entity.id();
        locale = entity.locale();
        title = entity.title();
        slug = entity.slug();
        content = entity.content();
        description = entity.description();
        image = this.imageMapper.toDto(entity.image());
        category = this.newsCategoryWebMapper.toDto(entity.category());
        authorName = entity.authorName();
        createdAt = entity.createdAt();
        NewsWebIndexDto newsWebIndexDto = new NewsWebIndexDto(id, locale, title, slug, content, description, image, category, authorName, createdAt);
        return newsWebIndexDto;
    }

    public NewsWebDto toFullDto(NewsSingleTranslationDto entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        TranslationLocale locale = null;
        String title = null;
        String slug = null;
        String content = null;
        String description = null;
        ImageDto image = null;
        NewsCategoryWebDto category = null;
        String authorName = null;
        LocalDateTime createdAt = null;
        id = entity.id();
        locale = entity.locale();
        title = entity.title();
        slug = entity.slug();
        content = entity.content();
        description = entity.description();
        image = this.imageMapper.toDto(entity.image());
        category = this.newsCategoryWebMapper.toDto(entity.category());
        authorName = entity.authorName();
        createdAt = entity.createdAt();
        NewsWebDto newsWebDto = new NewsWebDto(id, locale, title, slug, content, description, image, category, authorName, createdAt);
        return newsWebDto;
    }
}

