package com.tiu.turk.user.admin.mapper;

import com.tiu.turk.user.admin.dto.RoleDto;
import com.tiu.turk.user.common.entity.RoleEntity;

public interface RoleMapper {
    public RoleDto toDto(RoleEntity var1);
}

