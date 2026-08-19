package com.tiu.turk.contacts.web.mapper;

import com.tiu.turk.contacts.common.entity.FeedbackEntity;
import com.tiu.turk.contacts.web.dto.FeedbackCreateDto;
import com.tiu.turk.contacts.web.mapper.FeedbackMapper;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapperImpl
implements FeedbackMapper {
    public FeedbackEntity toEntity(FeedbackCreateDto dto) {
        if (dto == null) {
            return null;
        }
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setFullName(dto.fullName());
        feedbackEntity.setEmail(dto.email());
        feedbackEntity.setSubject(dto.subject());
        feedbackEntity.setMessage(dto.message());
        return feedbackEntity;
    }
}

