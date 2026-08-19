package com.tiu.turk.contacts.admin.dto;

import java.time.LocalDateTime;

public record FeedbackDto(Long id, String fullName, String email, String subject, String message, LocalDateTime createdAt) {
}
