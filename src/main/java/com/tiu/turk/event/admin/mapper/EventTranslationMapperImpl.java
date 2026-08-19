package com.tiu.turk.event.admin.mapper;

import com.tiu.turk.event.admin.dto.EventTranslationDto;
import com.tiu.turk.event.admin.dto.EventTranslationUpdateDto;
import com.tiu.turk.event.admin.mapper.EventTranslationMapper;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import org.springframework.stereotype.Component;

@Component
public class EventTranslationMapperImpl
implements EventTranslationMapper {
    public EventTranslationEntity toEntity(EventTranslationUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        EventTranslationEntity eventTranslationEntity = new EventTranslationEntity();
        eventTranslationEntity.setId(dto.getId());
        eventTranslationEntity.setTitle(dto.getTitle());
        eventTranslationEntity.setSlug(dto.getSlug());
        eventTranslationEntity.setDescription(dto.getDescription());
        return eventTranslationEntity;
    }

    public EventTranslationDto toDto(EventTranslationEntity entity) {
        if (entity == null) {
            return null;
        }
        EventTranslationDto eventTranslationDto = new EventTranslationDto();
        eventTranslationDto.setId(entity.getId());
        eventTranslationDto.setTitle(entity.getTitle());
        eventTranslationDto.setSlug(entity.getSlug());
        eventTranslationDto.setDescription(entity.getDescription());
        return eventTranslationDto;
    }
}

