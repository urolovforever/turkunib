package com.tiu.turk.user.admin.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.user.admin.dto.UserCreateDto;
import com.tiu.turk.user.admin.dto.UserDto;
import com.tiu.turk.user.admin.dto.UserUpdateDto;
import com.tiu.turk.user.admin.mapper.RoleMapper;
import com.tiu.turk.user.common.entity.RoleEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import java.util.HashSet;
import java.util.Set;

public interface UserMapper {
    public UserDto toDto(UserEntity var1);

    @Mapping(target="roles", expression="java(fromRoleId(dto.roleId()))")
    public UserEntity toEntity(UserCreateDto var1);

    @Mapping(target="roles", expression="java(fromRoleId(dto.roleId()))")
    public UserEntity toEntity(UserUpdateDto var1);

    default public Set<RoleEntity> fromRoleId(Long roleId) {
        HashSet<RoleEntity> roles = new HashSet<RoleEntity>();
        if (roleId != null) {
            RoleEntity role = new RoleEntity();
            role.setId(roleId);
            roles.add(role);
        }
        return roles;
    }
}

