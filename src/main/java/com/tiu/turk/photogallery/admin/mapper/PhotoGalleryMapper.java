package com.tiu.turk.photogallery.admin.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.photogallery.admin.dto.PhotoGalleryCreateDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryIndexDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryUpdateDto;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;

public interface PhotoGalleryMapper {
    @Mapping(target="imageCount", expression="java(entity.getImages() != null ? entity.getImages().size() : 0L)")
    public PhotoGalleryIndexDto toDto(PhotoGalleryEntity var1);

    public PhotoGalleryEntity toEntity(PhotoGalleryCreateDto var1);

    public PhotoGalleryEntity toEntity(PhotoGalleryUpdateDto var1);

    public PhotoGalleryDto toFullDto(PhotoGalleryEntity var1);
}

