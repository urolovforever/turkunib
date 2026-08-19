package com.tiu.turk.media.admin.mapper;

import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.media.admin.dto.MediaImageDto;
import com.tiu.turk.media.admin.mapper.MediaMapper;
import com.tiu.turk.user.admin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaMapperImpl
implements MediaMapper {
    @Autowired
    private UserMapper userMapper;

    public MediaImageDto toMediaImageDto(ImageEntity imageEntity) {
        if (imageEntity == null) {
            return null;
        }
        MediaImageDto mediaImageDto = new MediaImageDto();
        mediaImageDto.setId(imageEntity.getId());
        mediaImageDto.setImageName(imageEntity.getImageName());
        mediaImageDto.setImageFileName(imageEntity.getImageFileName());
        mediaImageDto.setImageContentType(imageEntity.getImageContentType());
        mediaImageDto.setImageSize(imageEntity.getImageSize());
        mediaImageDto.setAuthor(this.userMapper.toDto(imageEntity.getAuthor()));
        mediaImageDto.setCreatedByModule(imageEntity.getCreatedByModule());
        mediaImageDto.setCreatedAt(imageEntity.getCreatedAt());
        mediaImageDto.setUrl(this.getImageUrl(imageEntity));
        return mediaImageDto;
    }
}

