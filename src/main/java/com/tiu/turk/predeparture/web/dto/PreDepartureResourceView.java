package com.tiu.turk.predeparture.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.predeparture.common.entity.PreDepartureResourceEntity;
import com.tiu.turk.predeparture.common.entity.PreDepartureResourceTranslationEntity;
import lombok.Getter;

/**
 * Locale-aware read view of a pre-departure resource. Translatable text fields resolve to
 * the requested locale's translation when present (and non-blank), otherwise fall back to
 * the base English text stored on the entity. Non-translatable fields (id, file) pass
 * through from the base entity.
 */
@Getter
public class PreDepartureResourceView {
    private final Long id;
    private final String title;
    private final String description;
    private final FileEntity file;

    public PreDepartureResourceView(PreDepartureResourceEntity r, TranslationLocale locale) {
        PreDepartureResourceTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : r.getTranslations().get(locale);
        this.id = r.getId();
        this.title = pick(t == null ? null : t.getTitle(), r.getTitle());
        this.description = pick(t == null ? null : t.getDescription(), r.getDescription());
        this.file = r.getFile();
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
