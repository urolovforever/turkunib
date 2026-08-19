package com.tiu.turk.user.admin.dto;

import java.util.Objects;

public record UserCreateDto(String email, String firstName, String lastName, String password, Long roleId, Boolean enabled) {
}
