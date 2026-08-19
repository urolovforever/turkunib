package com.tiu.turk.media.admin.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.media.admin.dto.MediaImageDto;
import com.tiu.turk.user.admin.mapper.UserMapper;

public interface MediaMapper {
    @Mapping(target="url", expression="java(getImageUrl(imageEntity))")
    public MediaImageDto toMediaImageDto(ImageEntity var1);

    default public String getImageUrl(ImageEntity entity) {
        if (entity == null) {
            return null;
        }
        return "/uploads" + entity.getImagePath() + entity.getImageFileName();
    }
}

