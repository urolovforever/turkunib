package com.tiu.turk.event.admin.mapper;

import com.tiu.turk.event.admin.dto.EventTranslationDto;
import com.tiu.turk.event.admin.dto.EventTranslationUpdateDto;
import com.tiu.turk.event.common.entity.EventTranslationEntity;

public interface EventTranslationMapper {
    public EventTranslationEntity toEntity(EventTranslationUpdateDto var1);

    public EventTranslationDto toDto(EventTranslationEntity var1);
}

