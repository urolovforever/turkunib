package com.tiu.turk.banner.web.mapper;

import com.tiu.turk.banner.common.dto.BannerSingleTranslationDto;
import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BannerWebMapperImpl
implements BannerWebMapper {
    @Autowired
    private ImageMapper imageMapper;

    public BannerWebIndexDto toDto(BannerSingleTranslationDto entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        TranslationLocale locale = null;
        String title = null;
        String urlTitle = null;
        String shortTitle = null;
        String description = null;
        String url = null;
        Integer position = null;
        ImageDto image = null;
        id = entity.id();
        locale = entity.locale();
        title = entity.title();
        urlTitle = entity.urlTitle();
        shortTitle = entity.shortTitle();
        description = entity.description();
        url = entity.url();
        position = entity.position();
        image = this.imageMapper.toDto(entity.image());
        BannerWebIndexDto bannerWebIndexDto = new BannerWebIndexDto(id, locale, title, urlTitle, shortTitle, description, url, position, image);
        return bannerWebIndexDto;
    }
}

