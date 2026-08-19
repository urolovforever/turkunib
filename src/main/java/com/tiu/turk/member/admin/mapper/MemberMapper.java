package com.tiu.turk.member.admin.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.admin.dto.member.MemberCreateDto;
import com.tiu.turk.member.admin.dto.member.MemberDto;
import com.tiu.turk.member.admin.dto.member.MemberUpdateDto;
import com.tiu.turk.member.admin.mapper.CountryMapper;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;

public interface MemberMapper {
    @Mapping(source="countryId", target="country")
    public MemberEntity toEntity(MemberCreateDto var1);

    @Mapping(source="countryId", target="country")
    public MemberEntity toEntity(MemberUpdateDto var1);

    public MemberDto toDto(MemberEntity var1);
}

