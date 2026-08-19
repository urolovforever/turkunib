package com.tiu.turk.keydate.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.keydate.common.KeyDateType;
import com.tiu.turk.keydate.common.entity.KeyDateEntity;
import com.tiu.turk.keydate.common.entity.KeyDateTranslationEntity;
import java.time.LocalDate;
import lombok.Getter;

/**
 * Locale-aware read view of a key date. Translatable text fields resolve to the
 * requested locale's translation when present (and non-blank), otherwise fall back to
 * the base English text stored on the entity. Non-translatable fields (eventDate,
 * endDate, type, id) pass through from the base entity.
 */
@Getter
public class KeyDateView {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate eventDate;
    private final LocalDate endDate;
    private final KeyDateType type;

    public KeyDateView(KeyDateEntity k, TranslationLocale locale) {
        KeyDateTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : k.getTranslations().get(locale);
        this.id = k.getId();
        this.title = pick(t == null ? null : t.getTitle(), k.getTitle());
        this.description = pick(t == null ? null : t.getDescription(), k.getDescription());
        this.eventDate = k.getEventDate();
        this.endDate = k.getEndDate();
        this.type = k.getType();
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
