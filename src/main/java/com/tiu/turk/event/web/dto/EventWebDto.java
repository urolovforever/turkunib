package com.tiu.turk.event.web.dto;

import com.tiu.turk.event.web.dto.EventTranslationWebDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventWebDto(Long id, EventTranslationWebDto translation, LocalDateTime startAt, LocalDateTime endAt, String address, String placeName, BigDecimal latitude, BigDecimal longitude, String organizer, String format, String language) {
}
