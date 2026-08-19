package com.tiu.turk.member.web.mapper;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.member.web.dto.CountryWebDto;
import com.tiu.turk.member.web.mapper.CountryWebMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryWebMapperImpl
implements CountryWebMapper {
    @Autowired
    private ImageMapper imageMapper;

    public CountryWebDto toDto(CountryEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        String name = null;
        ImageDto image = null;
        id = entity.getId();
        name = entity.getName();
        image = this.imageMapper.toDto(entity.getImage());
        CountryWebDto countryWebDto = new CountryWebDto(id, name, image);
        return countryWebDto;
    }
}

