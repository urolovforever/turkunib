package com.tiu.turk.banner.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;

public record BannerWebIndexDto(Long id, TranslationLocale locale, String title, String urlTitle, String shortTitle, String description, String url, Integer position, ImageDto image) {
}
