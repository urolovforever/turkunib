package com.tiu.turk.user.admin.mapper;

import com.tiu.turk.user.admin.dto.RoleDto;
import com.tiu.turk.user.admin.mapper.RoleMapper;
import com.tiu.turk.user.common.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperImpl
implements RoleMapper {
    public RoleDto toDto(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        String name = null;
        String fullName = null;
        id = entity.getId();
        name = entity.getName();
        fullName = entity.getFullName();
        RoleDto roleDto = new RoleDto(id, name, fullName);
        return roleDto;
    }
}

