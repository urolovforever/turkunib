package com.tiu.turk.photogallery.admin.mapper;

import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationUpdateDto;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;

public interface PhotoGalleryTranslationMapper {
    public PhotoGalleryTranslationEntity toEntity(PhotoGalleryTranslationUpdateDto var1);

    public PhotoGalleryTranslationDto toDto(PhotoGalleryTranslationEntity var1);
}

