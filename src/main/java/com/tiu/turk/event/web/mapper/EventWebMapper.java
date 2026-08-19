package com.tiu.turk.event.web.mapper;

import org.mapstruct.Mapping;

import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.event.web.dto.EventTranslationWebDto;
import com.tiu.turk.event.web.dto.EventWebDto;

public interface EventWebMapper {
    @Mapping(target="translation", expression="java(getFirstTranslation(entity))")
    public EventWebDto toDto(EventEntity var1);

    default public EventTranslationWebDto getFirstTranslation(EventEntity entity) {
        if (entity.getTranslations() != null && !entity.getTranslations().isEmpty()) {
            EventTranslationEntity translation = (EventTranslationEntity)entity.getTranslations().values().iterator().next();
            return new EventTranslationWebDto(translation.getTitle(), translation.getSlug(), translation.getDescription());
        }
        return new EventTranslationWebDto("", "", "");
    }
}

