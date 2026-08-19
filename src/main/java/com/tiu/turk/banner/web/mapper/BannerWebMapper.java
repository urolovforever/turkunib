package com.tiu.turk.banner.web.mapper;

import com.tiu.turk.banner.common.dto.BannerSingleTranslationDto;
import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.image.mapper.ImageMapper;

public interface BannerWebMapper {
    public BannerWebIndexDto toDto(BannerSingleTranslationDto var1);
}

