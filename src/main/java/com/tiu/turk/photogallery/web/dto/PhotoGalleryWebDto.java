package com.tiu.turk.photogallery.web.dto;

import com.tiu.turk.image.dto.ImageDto;
import java.time.LocalDateTime;
import java.util.List;

public record PhotoGalleryWebDto(Long id, String title, String slug, String description, ImageDto previewImage, List<ImageDto> images, LocalDateTime createdAt) {
}
