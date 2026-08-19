package com.tiu.turk.member.admin.mapper;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.admin.dto.country.CountryCreateDto;
import com.tiu.turk.member.admin.dto.country.CountryDto;
import com.tiu.turk.member.admin.dto.country.CountryShortDto;
import com.tiu.turk.member.admin.dto.country.CountryUpdateDto;
import com.tiu.turk.member.admin.mapper.CountryMapper;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryMapperImpl
implements CountryMapper {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private UserMapper userMapper;

    public CountryDto toDto(CountryEntity entity) {
        if (entity == null) {
            return null;
        }
        CountryDto countryDto = new CountryDto();
        countryDto.setId(entity.getId());
        countryDto.setName(entity.getName());
        countryDto.setImage(this.imageMapper.toDto(entity.getImage()));
        countryDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        countryDto.setCreatedAt(entity.getCreatedAt());
        countryDto.setUpdatedAt(entity.getUpdatedAt());
        return countryDto;
    }

    public CountryShortDto toShortDto(CountryEntity entity) {
        if (entity == null) {
            return null;
        }
        CountryShortDto countryShortDto = new CountryShortDto();
        countryShortDto.setId(entity.getId());
        countryShortDto.setName(entity.getName());
        return countryShortDto;
    }

    public CountryEntity toEntity(CountryCreateDto createDto) {
        if (createDto == null) {
            return null;
        }
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(createDto.getName());
        return countryEntity;
    }

    public CountryEntity toEntity(CountryUpdateDto updateDto) {
        if (updateDto == null) {
            return null;
        }
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(updateDto.getId());
        countryEntity.setName(updateDto.getName());
        return countryEntity;
    }

    public CountryEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(id);
        return countryEntity;
    }
}

