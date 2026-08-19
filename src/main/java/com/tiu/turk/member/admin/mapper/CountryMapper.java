package com.tiu.turk.member.admin.mapper;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.admin.dto.country.CountryCreateDto;
import com.tiu.turk.member.admin.dto.country.CountryDto;
import com.tiu.turk.member.admin.dto.country.CountryShortDto;
import com.tiu.turk.member.admin.dto.country.CountryUpdateDto;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;

public interface CountryMapper {
    public CountryDto toDto(CountryEntity var1);

    public CountryShortDto toShortDto(CountryEntity var1);

    public CountryEntity toEntity(CountryCreateDto var1);

    public CountryEntity toEntity(CountryUpdateDto var1);

    public CountryEntity fromId(Long var1);
}

