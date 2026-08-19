package com.tiu.turk.application.admin.mapper;

import com.tiu.turk.application.admin.dto.ApplicationDto;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;

public interface ApplicationMapper {
    public ApplicationDto toDto(ApplicationEntity var1);
}

