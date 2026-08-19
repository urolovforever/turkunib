package com.tiu.turk.banner.admin.mapper;

import com.tiu.turk.banner.admin.dto.BannerCreateDto;
import com.tiu.turk.banner.admin.dto.BannerDto;
import com.tiu.turk.banner.admin.dto.BannerTranslationDto;
import com.tiu.turk.banner.admin.dto.BannerTranslationUpdateDto;
import com.tiu.turk.banner.admin.dto.BannerUpdateDto;
import com.tiu.turk.banner.admin.mapper.BannerMapper;
import com.tiu.turk.banner.admin.mapper.BannerTranslationMapper;
import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BannerMapperImpl
implements BannerMapper {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private BannerTranslationMapper bannerTranslationMapper;
    @Autowired
    private UserMapper userMapper;

    public BannerEntity toEntity(BannerCreateDto createDto) {
        if (createDto == null) {
            return null;
        }
        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setUrl(createDto.getUrl());
        bannerEntity.setPosition(createDto.getPosition());
        bannerEntity.setEnabled(Boolean.valueOf(createDto.isEnabled()));
        bannerEntity.setTranslations(this.buildTranslations(createDto));
        return bannerEntity;
    }

    public BannerEntity toEntity(BannerUpdateDto updateDto) {
        if (updateDto == null) {
            return null;
        }
        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setId(updateDto.getId());
        bannerEntity.setTranslations(this.stringBannerTranslationUpdateDtoMapToTranslationLocaleBannerTranslationEntityMap(updateDto.getTranslations()));
        bannerEntity.setUrl(updateDto.getUrl());
        bannerEntity.setPosition(updateDto.getPosition());
        bannerEntity.setImage(this.imageDtoToImageEntity(updateDto.getImage()));
        bannerEntity.setEnabled(Boolean.valueOf(updateDto.isEnabled()));
        return bannerEntity;
    }

    public BannerDto toDto(BannerEntity entity) {
        if (entity == null) {
            return null;
        }
        BannerDto bannerDto = new BannerDto();
        bannerDto.setId(entity.getId());
        bannerDto.setUrl(entity.getUrl());
        bannerDto.setTranslations(this.translationLocaleBannerTranslationEntityMapToStringBannerTranslationDtoMap(entity.getTranslations()));
        bannerDto.setPosition(entity.getPosition());
        bannerDto.setImage(this.imageMapper.toDto(entity.getImage()));
        if (entity.getEnabled() != null) {
            bannerDto.setEnabled(entity.getEnabled().booleanValue());
        }
        bannerDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        bannerDto.setCreatedAt(entity.getCreatedAt());
        bannerDto.setUpdatedAt(entity.getUpdatedAt());
        return bannerDto;
    }

    protected Map<TranslationLocale, BannerTranslationEntity> stringBannerTranslationUpdateDtoMapToTranslationLocaleBannerTranslationEntityMap(Map<String, BannerTranslationUpdateDto> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<TranslationLocale, BannerTranslationEntity> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<String, BannerTranslationUpdateDto> entry : map.entrySet()) {
            TranslationLocale key = Enum.valueOf(TranslationLocale.class, entry.getKey());
            BannerTranslationEntity value = this.bannerTranslationMapper.toEntity(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }

    protected ImageEntity imageDtoToImageEntity(ImageDto imageDto) {
        if (imageDto == null) {
            return null;
        }
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(imageDto.getId());
        imageEntity.setImageName(imageDto.getImageName());
        imageEntity.setImageType(imageDto.getImageType());
        imageEntity.setImageContentType(imageDto.getImageContentType());
        return imageEntity;
    }

    protected Map<String, BannerTranslationDto> translationLocaleBannerTranslationEntityMapToStringBannerTranslationDtoMap(Map<TranslationLocale, BannerTranslationEntity> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<String, BannerTranslationDto> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<TranslationLocale, BannerTranslationEntity> entry : map.entrySet()) {
            String key = entry.getKey().name();
            BannerTranslationDto value = this.bannerTranslationMapper.toDto(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }
}

