package com.tiu.turk.webinar.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.webinar.common.entity.WebinarEntity;
import com.tiu.turk.webinar.common.entity.WebinarTranslationEntity;

/**
 * Locale-aware read view of a webinar. The wrapped {@link WebinarEntity} is a transient,
 * detached copy whose translatable text fields (title, description, content) resolve to the
 * requested locale's translation when present (and non-blank), otherwise fall back to the base
 * English text. Non-translatable fields (id, startAt, durationMinutes, onlineLink, capacity)
 * pass through unchanged from the base entity. Templates keep calling {@code v.webinar().getTitle()}.
 */
public record WebinarView(WebinarEntity webinar, long registrationCount, boolean full) {

    public static WebinarView of(WebinarEntity base, TranslationLocale locale, long registrationCount, boolean full) {
        return new WebinarView(localize(base, locale), registrationCount, full);
    }

    private static WebinarEntity localize(WebinarEntity s, TranslationLocale locale) {
        WebinarTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : s.getTranslations().get(locale);
        if (t == null) {
            return s;
        }
        WebinarEntity v = new WebinarEntity();
        v.setId(s.getId());
        v.setTitle(pick(t.getTitle(), s.getTitle()));
        v.setDescription(pick(t.getDescription(), s.getDescription()));
        v.setContent(pick(t.getContent(), s.getContent()));
        v.setStartAt(s.getStartAt());
        v.setDurationMinutes(s.getDurationMinutes());
        v.setOnlineLink(s.getOnlineLink());
        v.setCapacity(s.getCapacity());
        v.setEnabled(s.getEnabled());
        return v;
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
