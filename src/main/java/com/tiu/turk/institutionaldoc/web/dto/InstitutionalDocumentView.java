package com.tiu.turk.institutionaldoc.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.institutionaldoc.common.InstitutionalDocumentSection;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentEntity;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentTranslationEntity;
import lombok.Getter;

/**
 * Locale-aware read view of an institutional document. Translatable text fields resolve
 * to the requested locale's translation when present (and non-blank), otherwise fall back
 * to the base English text stored on the entity. Non-translatable fields (id, section,
 * documentYear, file) pass through from the base entity.
 */
@Getter
public class InstitutionalDocumentView {
    private final Long id;
    private final InstitutionalDocumentSection section;
    private final String title;
    private final Integer documentYear;
    private final String description;
    private final FileEntity file;

    public InstitutionalDocumentView(InstitutionalDocumentEntity d, TranslationLocale locale) {
        InstitutionalDocumentTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : d.getTranslations().get(locale);
        this.id = d.getId();
        this.section = d.getSection();
        this.title = pick(t == null ? null : t.getTitle(), d.getTitle());
        this.documentYear = d.getDocumentYear();
        this.description = pick(t == null ? null : t.getDescription(), d.getDescription());
        this.file = d.getFile();
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
