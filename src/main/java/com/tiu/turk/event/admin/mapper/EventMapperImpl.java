package com.tiu.turk.event.admin.mapper;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.admin.dto.EventCreateDto;
import com.tiu.turk.event.admin.dto.EventDto;
import com.tiu.turk.event.admin.dto.EventTranslationDto;
import com.tiu.turk.event.admin.dto.EventTranslationUpdateDto;
import com.tiu.turk.event.admin.dto.EventUpdateDto;
import com.tiu.turk.event.admin.mapper.EventMapper;
import com.tiu.turk.event.admin.mapper.EventTranslationMapper;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMapperImpl
implements EventMapper {
    @Autowired
    private EventTranslationMapper eventTranslationMapper;
    @Autowired
    private UserMapper userMapper;

    public EventDto toDto(EventEntity entity) {
        if (entity == null) {
            return null;
        }
        EventDto eventDto = new EventDto();
        eventDto.setId(entity.getId());
        eventDto.setTranslations(this.translationLocaleEventTranslationEntityMapToStringEventTranslationDtoMap(entity.getTranslations()));
        eventDto.setAddress(entity.getAddress());
        eventDto.setPlaceName(entity.getPlaceName());
        eventDto.setLatitude(entity.getLatitude());
        eventDto.setLongitude(entity.getLongitude());
        eventDto.setOrganizer(entity.getOrganizer());
        eventDto.setFormat(entity.getFormat());
        eventDto.setLanguage(entity.getLanguage());
        if (entity.getEnabled() != null) {
            eventDto.setEnabled(entity.getEnabled().booleanValue());
        }
        eventDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        eventDto.setCreatedAt(entity.getCreatedAt());
        eventDto.setUpdatedAt(entity.getUpdatedAt());
        eventDto.setStartAt(this.fromLocalDateTime(entity.getStartAt()));
        eventDto.setEndAt(this.fromLocalDateTime(entity.getEndAt()));
        return eventDto;
    }

    public EventEntity toEntity(EventCreateDto createDto) {
        if (createDto == null) {
            return null;
        }
        EventEntity eventEntity = new EventEntity();
        eventEntity.setOrganizer(createDto.getOrganizer());
        eventEntity.setLanguage(createDto.getLanguage());
        eventEntity.setFormat(createDto.getFormat());
        eventEntity.setLatitude(createDto.getLatitude());
        eventEntity.setLongitude(createDto.getLongitude());
        eventEntity.setPlaceName(createDto.getPlaceName());
        eventEntity.setAddress(createDto.getAddress());
        eventEntity.setEnabled(Boolean.valueOf(createDto.isEnabled()));
        eventEntity.setStartAt(this.toLocalDateTime(createDto.getStartAt()));
        eventEntity.setEndAt(this.toLocalDateTime(createDto.getEndAt()));
        eventEntity.setTranslations(this.buildTranslations(createDto));
        return eventEntity;
    }

    public EventEntity toEntity(EventUpdateDto updateDto) {
        if (updateDto == null) {
            return null;
        }
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(updateDto.getId());
        eventEntity.setTranslations(this.stringEventTranslationUpdateDtoMapToTranslationLocaleEventTranslationEntityMap(updateDto.getTranslations()));
        eventEntity.setOrganizer(updateDto.getOrganizer());
        eventEntity.setLanguage(updateDto.getLanguage());
        eventEntity.setFormat(updateDto.getFormat());
        eventEntity.setLatitude(updateDto.getLatitude());
        eventEntity.setLongitude(updateDto.getLongitude());
        eventEntity.setPlaceName(updateDto.getPlaceName());
        eventEntity.setAddress(updateDto.getAddress());
        eventEntity.setEnabled(Boolean.valueOf(updateDto.isEnabled()));
        eventEntity.setStartAt(this.toLocalDateTime(updateDto.getStartAt()));
        eventEntity.setEndAt(this.toLocalDateTime(updateDto.getEndAt()));
        return eventEntity;
    }

    protected Map<String, EventTranslationDto> translationLocaleEventTranslationEntityMapToStringEventTranslationDtoMap(Map<TranslationLocale, EventTranslationEntity> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<String, EventTranslationDto> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<TranslationLocale, EventTranslationEntity> entry : map.entrySet()) {
            String key = entry.getKey().name();
            EventTranslationDto value = this.eventTranslationMapper.toDto(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }

    protected Map<TranslationLocale, EventTranslationEntity> stringEventTranslationUpdateDtoMapToTranslationLocaleEventTranslationEntityMap(Map<String, EventTranslationUpdateDto> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap<TranslationLocale, EventTranslationEntity> map1 = LinkedHashMap.newLinkedHashMap(map.size());
        for (Map.Entry<String, EventTranslationUpdateDto> entry : map.entrySet()) {
            TranslationLocale key = Enum.valueOf(TranslationLocale.class, entry.getKey());
            EventTranslationEntity value = this.eventTranslationMapper.toEntity(entry.getValue());
            map1.put(key, value);
        }
        return map1;
    }
}

