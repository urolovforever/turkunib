package com.tiu.turk.event.web.mapper;

import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.web.dto.EventTranslationWebDto;
import com.tiu.turk.event.web.dto.EventWebDto;
import com.tiu.turk.event.web.mapper.EventWebMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class EventWebMapperImpl
implements EventWebMapper {
    public EventWebDto toDto(EventEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        LocalDateTime startAt = null;
        LocalDateTime endAt = null;
        String address = null;
        String placeName = null;
        BigDecimal latitude = null;
        BigDecimal longitude = null;
        String organizer = null;
        String format = null;
        String language = null;
        id = entity.getId();
        startAt = entity.getStartAt();
        endAt = entity.getEndAt();
        address = entity.getAddress();
        placeName = entity.getPlaceName();
        latitude = entity.getLatitude();
        longitude = entity.getLongitude();
        organizer = entity.getOrganizer();
        format = entity.getFormat();
        language = entity.getLanguage();
        EventTranslationWebDto translation = this.getFirstTranslation(entity);
        EventWebDto eventWebDto = new EventWebDto(id, translation, startAt, endAt, address, placeName, latitude, longitude, organizer, format, language);
        return eventWebDto;
    }
}

