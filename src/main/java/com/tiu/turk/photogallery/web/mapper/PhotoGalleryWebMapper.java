package com.tiu.turk.photogallery.web.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.photogallery.common.dto.PhotoGallerySingleTranslationDto;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import com.tiu.turk.photogallery.web.dto.PhotoGalleryWebDto;

public interface PhotoGalleryWebMapper {
    public PhotoGalleryWebDto toDto(PhotoGallerySingleTranslationDto var1);

    @Mappings(value={@Mapping(target="title", expression="java(getFirstTranslation(entity).getTitle())"), @Mapping(target="slug", expression="java(getFirstTranslation(entity).getSlug())"), @Mapping(target="description", expression="java(getFirstTranslation(entity).getDescription())")})
    public PhotoGalleryWebDto toDto(PhotoGalleryEntity var1);

    default public PhotoGalleryTranslationEntity getFirstTranslation(PhotoGalleryEntity entity) {
        if (entity.getTranslations() != null && !entity.getTranslations().isEmpty()) {
            PhotoGalleryTranslationEntity translation = (PhotoGalleryTranslationEntity)entity.getTranslations().values().iterator().next();
            return translation != null ? translation : new PhotoGalleryTranslationEntity();
        }
        return new PhotoGalleryTranslationEntity();
    }
}

