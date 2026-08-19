package com.tiu.turk.user.admin.mapper;

import com.tiu.turk.user.admin.dto.RoleDto;
import com.tiu.turk.user.admin.dto.UserCreateDto;
import com.tiu.turk.user.admin.dto.UserDto;
import com.tiu.turk.user.admin.dto.UserUpdateDto;
import com.tiu.turk.user.admin.mapper.RoleMapper;
import com.tiu.turk.user.admin.mapper.UserMapper;
import com.tiu.turk.user.common.entity.RoleEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl
implements UserMapper {
    @Autowired
    private RoleMapper roleMapper;

    public UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        Set roles = null;
        boolean enabled = false;
        LocalDateTime updatedAt = null;
        LocalDateTime createdAt = null;
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        roles = this.roleEntitySetToRoleDtoSet(entity.getRoles());
        if (entity.getEnabled() != null) {
            enabled = entity.getEnabled();
        }
        updatedAt = entity.getUpdatedAt();
        createdAt = entity.getCreatedAt();
        UserDto userDto = new UserDto(id, firstName, lastName, email, roles, enabled, updatedAt, createdAt);
        return userDto;
    }

    public UserEntity toEntity(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(dto.email());
        userEntity.setPassword(dto.password());
        userEntity.setFirstName(dto.firstName());
        userEntity.setLastName(dto.lastName());
        userEntity.setEnabled(dto.enabled());
        userEntity.setRoles(this.fromRoleId(dto.roleId()));
        return userEntity;
    }

    public UserEntity toEntity(UserUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(dto.id());
        userEntity.setEmail(dto.email());
        userEntity.setPassword(dto.password());
        userEntity.setFirstName(dto.firstName());
        userEntity.setLastName(dto.lastName());
        userEntity.setEnabled(dto.enabled());
        userEntity.setRoles(this.fromRoleId(dto.roleId()));
        return userEntity;
    }

    protected Set<RoleDto> roleEntitySetToRoleDtoSet(Set<RoleEntity> set) {
        if (set == null) {
            return null;
        }
        LinkedHashSet<RoleDto> set1 = LinkedHashSet.newLinkedHashSet(set.size());
        for (RoleEntity roleEntity : set) {
            set1.add(this.roleMapper.toDto(roleEntity));
        }
        return set1;
    }
}

