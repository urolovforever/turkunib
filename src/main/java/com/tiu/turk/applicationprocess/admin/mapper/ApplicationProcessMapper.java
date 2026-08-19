package com.tiu.turk.applicationprocess.admin.mapper;

import com.tiu.turk.applicationprocess.admin.dto.ApplicationProcessDto;
import com.tiu.turk.applicationprocess.common.entity.ApplicationProcessEntity;
import com.tiu.turk.files.FileMapper;
import com.tiu.turk.member.admin.mapper.MemberMapper;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.List;

public interface ApplicationProcessMapper {
    public ApplicationProcessDto toDto(ApplicationProcessEntity var1);

    public List<ApplicationProcessDto> toDtoList(List<ApplicationProcessEntity> var1);
}

