package com.tiu.turk.news.web.mapper;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.web.dto.category.NewsCategoryWebDto;
import com.tiu.turk.news.web.dto.category.NewsCategoryWebTranslationDto;
import com.tiu.turk.news.web.mapper.NewsCategoryWebMapper;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsCategoryWebMapperImpl
implements NewsCategoryWebMapper {
    @Autowired
    private ImageMapper imageMapper;

    public NewsCategoryWebDto toDto(NewsCategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        ImageDto image = null;
        LocalDateTime createdAt = null;
        id = entity.getId();
        image = this.imageMapper.toDto(entity.getImage());
        createdAt = entity.getCreatedAt();
        NewsCategoryWebTranslationDto translation = this.getFirstTranslation(entity);
        NewsCategoryWebDto newsCategoryWebDto = new NewsCategoryWebDto(id, image, translation, createdAt);
        return newsCategoryWebDto;
    }
}

