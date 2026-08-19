package com.tiu.turk.member.web.mapper;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.member.web.dto.CountryWebDto;

public interface CountryWebMapper {
    public CountryWebDto toDto(CountryEntity var1);
}

