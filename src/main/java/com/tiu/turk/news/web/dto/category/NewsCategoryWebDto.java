package com.tiu.turk.news.web.dto.category;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.news.web.dto.category.NewsCategoryWebTranslationDto;
import java.time.LocalDateTime;

public record NewsCategoryWebDto(Long id, ImageDto image, NewsCategoryWebTranslationDto translation, LocalDateTime createdAt) {
}
