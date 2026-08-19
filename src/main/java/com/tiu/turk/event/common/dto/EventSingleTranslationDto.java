package com.tiu.turk.event.common.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSingleTranslationDto(Long id, TranslationLocale locale, String title, String slug, String description, String organizer, LocalDateTime startAt, LocalDateTime endAt, String language, String format, BigDecimal latitude, BigDecimal longitude, String placeName, LocalDateTime createdAt) {
}
