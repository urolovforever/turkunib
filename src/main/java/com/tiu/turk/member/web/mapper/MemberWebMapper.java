package com.tiu.turk.member.web.mapper;

import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.web.dto.MemberWebDto;
import java.util.List;
import java.util.Map;

public interface MemberWebMapper {
    public MemberWebDto toDto(MemberEntity var1);

    public List<MemberWebDto> toDto(List<MemberEntity> var1);

    public Map<Long, List<MemberWebDto>> toDtoMap(Map<Long, List<MemberEntity>> var1);
}

