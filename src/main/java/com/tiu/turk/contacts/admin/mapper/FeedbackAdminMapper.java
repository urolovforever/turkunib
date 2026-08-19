package com.tiu.turk.contacts.admin.mapper;

import com.tiu.turk.contacts.admin.dto.FeedbackDto;
import com.tiu.turk.contacts.common.entity.FeedbackEntity;

public interface FeedbackAdminMapper {
    public FeedbackDto toDto(FeedbackEntity var1);
}

