package com.tiu.turk.contacts.web.mapper;

import com.tiu.turk.contacts.common.entity.FeedbackEntity;
import com.tiu.turk.contacts.web.dto.FeedbackCreateDto;

public interface FeedbackMapper {
    public FeedbackEntity toEntity(FeedbackCreateDto var1);
}

