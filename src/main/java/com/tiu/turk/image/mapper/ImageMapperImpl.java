package com.tiu.turk.image.mapper;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.mapper.ImageMapper;
import org.springframework.stereotype.Component;

@Component
public class ImageMapperImpl
implements ImageMapper {
    public ImageDto toDto(ImageEntity entity) {
        if (entity == null) {
            return null;
        }
        ImageDto imageDto = new ImageDto();
        imageDto.setId(entity.getId());
        imageDto.setImageType(entity.getImageType());
        imageDto.setImageContentType(entity.getImageContentType());
        imageDto.setImageName(entity.getImageName());
        imageDto.setUrl(this.getImageUrl(entity));
        return imageDto;
    }
}

