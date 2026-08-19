package com.tiu.turk.banner.common.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import java.time.LocalDateTime;

public record BannerSingleTranslationDto(Long id, TranslationLocale locale, String title, String urlTitle, String shortTitle, String description, String url, Integer position, ImageEntity image, LocalDateTime createdAt) {
}
