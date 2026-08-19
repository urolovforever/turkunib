package com.tiu.turk.image.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;

public interface ImageMapper {
    @Mapping(target="url", expression="java(getImageUrl(entity))")
    public ImageDto toDto(ImageEntity var1);

    default public String getImageUrl(ImageEntity entity) {
        if (entity == null) {
            return null;
        }
        return "/uploads" + entity.getImagePath() + entity.getImageFileName();
    }
}

