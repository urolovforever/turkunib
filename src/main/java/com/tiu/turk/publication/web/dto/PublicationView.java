package com.tiu.turk.publication.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.publication.common.PublicationCategory;
import com.tiu.turk.publication.common.entity.PublicationEntity;
import com.tiu.turk.publication.common.entity.PublicationTranslationEntity;
import lombok.Getter;

/**
 * Locale-aware read view of a publication. Translatable text fields resolve to the
 * requested locale's translation when present (and non-blank), otherwise fall back to
 * the base English text stored on the entity. Non-translatable fields (category,
 * publicationYear, file, id) pass through from the base entity.
 */
@Getter
public class PublicationView {
    private final Long id;
    private final String title;
    private final String description;
    private final PublicationCategory category;
    private final Integer publicationYear;
    private final FileEntity file;

    public PublicationView(PublicationEntity p, TranslationLocale locale) {
        PublicationTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : p.getTranslations().get(locale);
        this.id = p.getId();
        this.title = pick(t == null ? null : t.getTitle(), p.getTitle());
        this.description = pick(t == null ? null : t.getDescription(), p.getDescription());
        this.category = p.getCategory();
        this.publicationYear = p.getPublicationYear();
        this.file = p.getFile();
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
