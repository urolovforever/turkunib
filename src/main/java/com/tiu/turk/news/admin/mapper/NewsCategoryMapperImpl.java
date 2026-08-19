package com.tiu.turk.news.admin.mapper;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.admin.dto.category.NewsCategoryCreateDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryTranslationUpdateDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsCategoryMapper;
import com.tiu.turk.news.admin.mapper.NewsCategoryTranslationMapper;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsCategoryMapperImpl
implements NewsCategoryMapper {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private NewsCategoryTranslationMapper newsCategoryTranslationMapper;
    @Autowired
    private UserMapper userMapper;

    public NewsCategoryEntity toEntity(NewsCategoryCreateDto dto) {
        if (dto == null) {
            return null;
        }
        NewsCategoryEntity newsCategoryEntity = new NewsCategoryEntity();
        newsCategoryEntity.setId(dto.getId());
        newsCategoryEntity.setTranslations(this.buildTranslations(dto));
        return newsCategoryEntity;
    }

    public NewsCategoryEntity toEntity(NewsCategoryUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        NewsCategoryEntity newsCategoryEntity = new NewsCategoryEntity();
        newsCategoryEntity.setId(dto.getId());
        newsCategoryEntity.setImage(this.imageDtoToImageEntity(dto.getImage()));
        newsCategoryEntity.setTranslations(this.stringNewsCategoryTranslationUpdateDtoMapToTranslationLocaleNewsCategoryTranslationEntityMap(dto.getTranslations()));
        return newsCategoryEntity;
    }

    public NewsCategoryDto toDto(NewsCategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        NewsCategoryDto newsCategoryDto = new NewsCategoryDto();
        newsCategoryDto.setId(entity.getId());
        newsCategoryDto.setImage(this.imageMapper.toDto(entity.getImage()));
        newsCategoryDto.setTranslations(this.translationLocaleNewsCategoryTranslationEntityMapToStringNewsCategoryTranslationDtoMap(entity.getTranslations()));
        newsCategoryDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        newsCategoryDto.setCreatedAt(entity.getCreatedAt());
        newsCategoryDto.setUpdatedAt(entity.getUpdatedAt());
        return newsCategoryDto;
    }

    public NewsCategoryEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        NewsCategoryEntity newsCategoryEntity = new NewsCategoryEntity();
        newsCategoryEntity.setId(id);
        return newsCategoryEntity;
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

    protected NewsCategoryTranslationEntity newsCategoryTranslationUpdateDtoToNewsCategoryTranslationEntity(NewsCategoryTranslationUpdateDto newsCategoryTranslationUpdateDto) {
        if (newsCategoryTranslationUpdateDto == null) {
            return null;
        }
        NewsCategoryTranslationEntity newsCategoryTranslationEntity = new NewsCategoryTranslationEntity();
        newsCategoryTranslationEntity.setId(newsCategoryTranslationUpdateDto.getId());
        newsCategoryTranslationEntity.setTitle(newsCategoryTranslationUpdateDto.getTitle());
        newsCategoryTranslationEntity.setSlug(newsCategoryTranslationUpdateDto.getSlug());
        newsCategoryTranslationEntity.setDescription(newsCategoryTranslationUpdateDto.getDescription());
        return newsCategoryTranslationEntity;
    }

    protected Map<TranslationLocale, NewsCategoryTranslationEntity> stringNewsCategoryTranslationUpdateDtoMapToTranslationLocaleNewsCategoryTranslationEntityMap(Map<String, NewsCategoryTranslationUpdateDto> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<TranslationLocale, NewsCategoryTranslationEntity> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<String, NewsCategoryTranslationUpdateDto> entry : map.entrySet()) {
            TranslationLocale key = Enum.valueOf(TranslationLocale.class, entry.getKey());
            NewsCategoryTranslationEntity value = this.newsCategoryTranslationUpdateDtoToNewsCategoryTranslationEntity(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }

    protected Map<String, NewsCategoryTranslationDto> translationLocaleNewsCategoryTranslationEntityMapToStringNewsCategoryTranslationDtoMap(Map<TranslationLocale, NewsCategoryTranslationEntity> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<String, NewsCategoryTranslationDto> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<TranslationLocale, NewsCategoryTranslationEntity> entry : map.entrySet()) {
            String key = entry.getKey().name();
            NewsCategoryTranslationDto value = this.newsCategoryTranslationMapper.toDto(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }
}

