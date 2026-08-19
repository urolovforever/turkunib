package com.tiu.turk.photogallery.admin.mapper;

import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationUpdateDto;
import com.tiu.turk.photogallery.admin.mapper.PhotoGalleryTranslationMapper;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import org.springframework.stereotype.Component;

@Component
public class PhotoGalleryTranslationMapperImpl
implements PhotoGalleryTranslationMapper {
    public PhotoGalleryTranslationEntity toEntity(PhotoGalleryTranslationUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        PhotoGalleryTranslationEntity photoGalleryTranslationEntity = new PhotoGalleryTranslationEntity();
        photoGalleryTranslationEntity.setId(dto.getId());
        photoGalleryTranslationEntity.setTitle(dto.getTitle());
        photoGalleryTranslationEntity.setSlug(dto.getSlug());
        photoGalleryTranslationEntity.setDescription(dto.getDescription());
        return photoGalleryTranslationEntity;
    }

    public PhotoGalleryTranslationDto toDto(PhotoGalleryTranslationEntity entity) {
        if (entity == null) {
            return null;
        }
        PhotoGalleryTranslationDto photoGalleryTranslationDto = new PhotoGalleryTranslationDto();
        photoGalleryTranslationDto.setId(entity.getId());
        photoGalleryTranslationDto.setTitle(entity.getTitle());
        photoGalleryTranslationDto.setSlug(entity.getSlug());
        photoGalleryTranslationDto.setDescription(entity.getDescription());
        return photoGalleryTranslationDto;
    }
}

