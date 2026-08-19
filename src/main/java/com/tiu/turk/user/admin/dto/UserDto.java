package com.tiu.turk.user.admin.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserDto(Long id, String firstName, String lastName, String email, Set<RoleDto> roles, boolean enabled, LocalDateTime updatedAt, LocalDateTime createdAt) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
