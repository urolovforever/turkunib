package com.tiu.turk.news.web.dto.news;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.news.web.dto.category.NewsCategoryWebDto;
import java.time.LocalDateTime;

public record NewsWebIndexDto(Long id, TranslationLocale locale, String title, String slug, String content, String description, ImageDto image, NewsCategoryWebDto category, String authorName, LocalDateTime createdAt) {
}
