package com.tiu.turk.event.admin.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.admin.dto.EventCreateDto;
import com.tiu.turk.event.admin.dto.EventDto;
import com.tiu.turk.event.admin.dto.EventTranslationUpdateDto;
import com.tiu.turk.event.admin.dto.EventUpdateDto;
import com.tiu.turk.event.admin.mapper.EventTranslationMapper;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;

public interface EventMapper {
    @Mappings(value={@Mapping(target="startAt", expression="java(fromLocalDateTime(entity.getStartAt()))"), @Mapping(target="endAt", expression="java(fromLocalDateTime(entity.getEndAt()))")})
    public EventDto toDto(EventEntity var1);

    @Mappings(value={@Mapping(target="startAt", expression="java(toLocalDateTime(createDto.getStartAt()))"), @Mapping(target="endAt", expression="java(toLocalDateTime(createDto.getEndAt()))"), @Mapping(target="translations", expression="java(buildTranslations(createDto))")})
    public EventEntity toEntity(EventCreateDto var1);

    @Mappings(value={@Mapping(target="startAt", expression="java(toLocalDateTime(updateDto.getStartAt()))"), @Mapping(target="endAt", expression="java(toLocalDateTime(updateDto.getEndAt()))")})
    public EventEntity toEntity(EventUpdateDto var1);

    default public LocalDateTime toLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    default public String fromLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(formatter);
    }

    default public Map<TranslationLocale, EventTranslationEntity> buildTranslations(EventCreateDto dto) {
        EnumMap<TranslationLocale, EventTranslationEntity> map = new EnumMap<TranslationLocale, EventTranslationEntity>(TranslationLocale.class);
        if (dto == null || dto.getTranslations() == null) {
            return map;
        }
        for (Map.Entry<String, EventTranslationUpdateDto> entry : dto.getTranslations().entrySet()) {
            EventTranslationUpdateDto translationDto = entry.getValue();
            if (translationDto == null) {
                continue;
            }
            TranslationLocale locale = TranslationLocale.valueOf(entry.getKey());
            EventTranslationEntity translation = new EventTranslationEntity();
            translation.setTitle(translationDto.getTitle());
            translation.setSlug(translationDto.getSlug());
            translation.setDescription(translationDto.getDescription());
            translation.setLocale(locale);
            map.put(locale, translation);
        }
        return map;
    }
}

