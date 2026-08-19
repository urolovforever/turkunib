package com.tiu.turk.photogallery.web.mapper;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.photogallery.common.dto.PhotoGallerySingleTranslationDto;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.web.dto.PhotoGalleryWebDto;
import com.tiu.turk.photogallery.web.mapper.PhotoGalleryWebMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhotoGalleryWebMapperImpl
implements PhotoGalleryWebMapper {
    @Autowired
    private ImageMapper imageMapper;

    public PhotoGalleryWebDto toDto(PhotoGallerySingleTranslationDto entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        String title = null;
        String slug = null;
        String description = null;
        ImageDto previewImage = null;
        List images = null;
        LocalDateTime createdAt = null;
        id = entity.id();
        title = entity.title();
        slug = entity.slug();
        description = entity.description();
        previewImage = this.imageMapper.toDto(entity.previewImage());
        images = this.imageEntityListToImageDtoList(entity.images());
        createdAt = entity.createdAt();
        PhotoGalleryWebDto photoGalleryWebDto = new PhotoGalleryWebDto(id, title, slug, description, previewImage, images, createdAt);
        return photoGalleryWebDto;
    }

    public PhotoGalleryWebDto toDto(PhotoGalleryEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        ImageDto previewImage = null;
        List images = null;
        LocalDateTime createdAt = null;
        id = entity.getId();
        previewImage = this.imageMapper.toDto(entity.getPreviewImage());
        images = this.imageEntityListToImageDtoList(entity.getImages());
        createdAt = entity.getCreatedAt();
        String title = this.getFirstTranslation(entity).getTitle();
        String slug = this.getFirstTranslation(entity).getSlug();
        String description = this.getFirstTranslation(entity).getDescription();
        PhotoGalleryWebDto photoGalleryWebDto = new PhotoGalleryWebDto(id, title, slug, description, previewImage, images, createdAt);
        return photoGalleryWebDto;
    }

    protected List<ImageDto> imageEntityListToImageDtoList(List<ImageEntity> list) {
        if (list == null) {
            return null;
        }
        ArrayList<ImageDto> list1 = new ArrayList<ImageDto>(list.size());
        for (ImageEntity imageEntity : list) {
            list1.add(this.imageMapper.toDto(imageEntity));
        }
        return list1;
    }
}

