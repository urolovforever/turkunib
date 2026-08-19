package com.tiu.turk.photogallery.common.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import java.time.LocalDateTime;
import java.util.List;

public record PhotoGallerySingleTranslationDto(Long id, TranslationLocale locale, String title, String slug, String description, ImageEntity previewImage, List<ImageEntity> images, LocalDateTime createdAt) {
}
