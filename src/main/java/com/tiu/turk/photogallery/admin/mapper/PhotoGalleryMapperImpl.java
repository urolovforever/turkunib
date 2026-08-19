package com.tiu.turk.photogallery.admin.mapper;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryCreateDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryIndexDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationUpdateDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryUpdateDto;
import com.tiu.turk.photogallery.admin.mapper.PhotoGalleryMapper;
import com.tiu.turk.photogallery.admin.mapper.PhotoGalleryTranslationMapper;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhotoGalleryMapperImpl
implements PhotoGalleryMapper {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private PhotoGalleryTranslationMapper photoGalleryTranslationMapper;
    @Autowired
    private UserMapper userMapper;

    public PhotoGalleryIndexDto toDto(PhotoGalleryEntity entity) {
        if (entity == null) {
            return null;
        }
        PhotoGalleryIndexDto photoGalleryIndexDto = new PhotoGalleryIndexDto();
        photoGalleryIndexDto.setId(entity.getId());
        photoGalleryIndexDto.setTranslations(this.translationLocalePhotoGalleryTranslationEntityMapToStringPhotoGalleryTranslationDtoMap(entity.getTranslations()));
        photoGalleryIndexDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        if (entity.getEnabled() != null) {
            photoGalleryIndexDto.setEnabled(entity.getEnabled().booleanValue());
        }
        photoGalleryIndexDto.setCreatedAt(entity.getCreatedAt());
        photoGalleryIndexDto.setUpdatedAt(entity.getUpdatedAt());
        photoGalleryIndexDto.setImageCount(Long.valueOf(entity.getImages() != null ? (long)entity.getImages().size() : 0L));
        return photoGalleryIndexDto;
    }

    public PhotoGalleryEntity toEntity(PhotoGalleryCreateDto dto) {
        if (dto == null) {
            return null;
        }
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity();
        photoGalleryEntity.setTranslations(this.stringPhotoGalleryTranslationUpdateDtoMapToTranslationLocalePhotoGalleryTranslationEntityMap(dto.getTranslations()));
        photoGalleryEntity.setEnabled(Boolean.valueOf(dto.isEnabled()));
        return photoGalleryEntity;
    }

    public PhotoGalleryEntity toEntity(PhotoGalleryUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        PhotoGalleryEntity photoGalleryEntity = new PhotoGalleryEntity();
        photoGalleryEntity.setId(dto.getId());
        photoGalleryEntity.setTranslations(this.stringPhotoGalleryTranslationUpdateDtoMapToTranslationLocalePhotoGalleryTranslationEntityMap(dto.getTranslations()));
        photoGalleryEntity.setEnabled(Boolean.valueOf(dto.isEnabled()));
        return photoGalleryEntity;
    }

    public PhotoGalleryDto toFullDto(PhotoGalleryEntity entity) {
        if (entity == null) {
            return null;
        }
        PhotoGalleryDto photoGalleryDto = new PhotoGalleryDto();
        photoGalleryDto.setId(entity.getId());
        photoGalleryDto.setTranslations(this.translationLocalePhotoGalleryTranslationEntityMapToStringPhotoGalleryTranslationDtoMap(entity.getTranslations()));
        photoGalleryDto.setPreviewImage(this.imageMapper.toDto(entity.getPreviewImage()));
        photoGalleryDto.setImages(this.imageEntityListToImageDtoList(entity.getImages()));
        photoGalleryDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        if (entity.getEnabled() != null) {
            photoGalleryDto.setEnabled(entity.getEnabled().booleanValue());
        }
        photoGalleryDto.setCreatedAt(entity.getCreatedAt());
        photoGalleryDto.setUpdatedAt(entity.getUpdatedAt());
        return photoGalleryDto;
    }

    protected Map<String, PhotoGalleryTranslationDto> translationLocalePhotoGalleryTranslationEntityMapToStringPhotoGalleryTranslationDtoMap(Map<TranslationLocale, PhotoGalleryTranslationEntity> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<String, PhotoGalleryTranslationDto> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<TranslationLocale, PhotoGalleryTranslationEntity> entry : map.entrySet()) {
            String key = entry.getKey().name();
            PhotoGalleryTranslationDto value = this.photoGalleryTranslationMapper.toDto(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }

    protected Map<TranslationLocale, PhotoGalleryTranslationEntity> stringPhotoGalleryTranslationUpdateDtoMapToTranslationLocalePhotoGalleryTranslationEntityMap(Map<String, PhotoGalleryTranslationUpdateDto> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<TranslationLocale, PhotoGalleryTranslationEntity> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<String, PhotoGalleryTranslationUpdateDto> entry : map.entrySet()) {
            TranslationLocale key = Enum.valueOf(TranslationLocale.class, entry.getKey());
            PhotoGalleryTranslationEntity value = this.photoGalleryTranslationMapper.toEntity(entry.getValue());
            map1.put(key, value);
        }
        return map1;
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

