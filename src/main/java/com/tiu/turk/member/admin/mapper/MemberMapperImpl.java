package com.tiu.turk.member.admin.mapper;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.admin.dto.member.MemberCreateDto;
import com.tiu.turk.member.admin.dto.member.MemberDto;
import com.tiu.turk.member.admin.dto.member.MemberUpdateDto;
import com.tiu.turk.member.admin.mapper.CountryMapper;
import com.tiu.turk.member.admin.mapper.MemberMapper;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberMapperImpl
implements MemberMapper {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private UserMapper userMapper;

    public MemberEntity toEntity(MemberCreateDto createDto) {
        if (createDto == null) {
            return null;
        }
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setCountry(this.countryMapper.fromId(createDto.getCountryId()));
        memberEntity.setName(createDto.getName());
        memberEntity.setFounded(createDto.getFounded());
        memberEntity.setAddress(createDto.getAddress());
        memberEntity.setUrl(createDto.getUrl());
        memberEntity.setStudentsNumber(createDto.getStudentsNumber());
        memberEntity.setFacultiesNumber(createDto.getFacultiesNumber());
        memberEntity.setLatitude(createDto.getLatitude());
        memberEntity.setLongitude(createDto.getLongitude());
        memberEntity.setAdditionalInfo(createDto.getAdditionalInfo());
        memberEntity.setEnabled(Boolean.valueOf(createDto.isEnabled()));
        return memberEntity;
    }

    public MemberEntity toEntity(MemberUpdateDto memberUpdateDto) {
        if (memberUpdateDto == null) {
            return null;
        }
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setCountry(this.countryMapper.fromId(memberUpdateDto.getCountryId()));
        memberEntity.setId(memberUpdateDto.getId());
        memberEntity.setName(memberUpdateDto.getName());
        memberEntity.setImage(this.imageDtoToImageEntity(memberUpdateDto.getImage()));
        memberEntity.setFounded(memberUpdateDto.getFounded());
        memberEntity.setAddress(memberUpdateDto.getAddress());
        memberEntity.setUrl(memberUpdateDto.getUrl());
        memberEntity.setStudentsNumber(memberUpdateDto.getStudentsNumber());
        memberEntity.setFacultiesNumber(memberUpdateDto.getFacultiesNumber());
        memberEntity.setLatitude(memberUpdateDto.getLatitude());
        memberEntity.setLongitude(memberUpdateDto.getLongitude());
        memberEntity.setAdditionalInfo(memberUpdateDto.getAdditionalInfo());
        memberEntity.setEnabled(Boolean.valueOf(memberUpdateDto.isEnabled()));
        return memberEntity;
    }

    public MemberDto toDto(MemberEntity entity) {
        if (entity == null) {
            return null;
        }
        MemberDto memberDto = new MemberDto();
        memberDto.setId(entity.getId());
        memberDto.setName(entity.getName());
        memberDto.setImage(this.imageMapper.toDto(entity.getImage()));
        memberDto.setFounded(entity.getFounded());
        memberDto.setAddress(entity.getAddress());
        memberDto.setUrl(entity.getUrl());
        memberDto.setStudentsNumber(entity.getStudentsNumber());
        memberDto.setFacultiesNumber(entity.getFacultiesNumber());
        memberDto.setCountry(this.countryMapper.toDto(entity.getCountry()));
        memberDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        memberDto.setLatitude(entity.getLatitude());
        memberDto.setLongitude(entity.getLongitude());
        memberDto.setAdditionalInfo(entity.getAdditionalInfo());
        if (entity.getEnabled() != null) {
            memberDto.setEnabled(entity.getEnabled().booleanValue());
        }
        memberDto.setCreatedAt(entity.getCreatedAt());
        memberDto.setUpdatedAt(entity.getUpdatedAt());
        return memberDto;
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
}

