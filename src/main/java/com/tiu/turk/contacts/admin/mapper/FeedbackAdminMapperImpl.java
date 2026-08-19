package com.tiu.turk.contacts.admin.mapper;

import com.tiu.turk.contacts.admin.dto.FeedbackDto;
import com.tiu.turk.contacts.admin.mapper.FeedbackAdminMapper;
import com.tiu.turk.contacts.common.entity.FeedbackEntity;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class FeedbackAdminMapperImpl
implements FeedbackAdminMapper {
    public FeedbackDto toDto(FeedbackEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        String fullName = null;
        String email = null;
        String subject = null;
        String message = null;
        LocalDateTime createdAt = null;
        id = entity.getId();
        fullName = entity.getFullName();
        email = entity.getEmail();
        subject = entity.getSubject();
        message = entity.getMessage();
        createdAt = entity.getCreatedAt();
        FeedbackDto feedbackDto = new FeedbackDto(id, fullName, email, subject, message, createdAt);
        return feedbackDto;
    }
}

