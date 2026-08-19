package com.tiu.turk.application.admin.dto;

import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;

public record ApplicationDto(Long id, String fullName, String studentId, String institution, String department, String phoneNumber, UserDto author, LocalDateTime updatedAt, LocalDateTime createdAt) {
}
