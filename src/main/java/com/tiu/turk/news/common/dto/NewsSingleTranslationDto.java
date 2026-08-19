package com.tiu.turk.news.common.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import java.time.LocalDateTime;

public record NewsSingleTranslationDto(Long id, TranslationLocale locale, String title, String slug, String content, String description, ImageEntity image, NewsCategoryEntity category, String authorName, LocalDateTime createdAt) {
}
