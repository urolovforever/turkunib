package com.tiu.turk.research.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.research.common.ResearchProjectStatus;
import com.tiu.turk.research.common.entity.ResearchProjectEntity;
import com.tiu.turk.research.common.entity.ResearchProjectTranslationEntity;
import lombok.Getter;

/**
 * Locale-aware read view of a research project. Translatable text fields resolve to the
 * requested locale's translation when present (and non-blank), otherwise fall back to
 * the base English text stored on the entity. Non-translatable fields (id, startYear,
 * endYear, status) pass through from the base entity.
 */
@Getter
public class ResearchProjectView {
    private final Long id;
    private final String title;
    private final String subjectArea;
    private final String description;
    private final String content;
    private final String participatingUniversities;
    private final Integer startYear;
    private final Integer endYear;
    private final ResearchProjectStatus status;

    public ResearchProjectView(ResearchProjectEntity p, TranslationLocale locale) {
        ResearchProjectTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : p.getTranslations().get(locale);
        this.id = p.getId();
        this.title = pick(t == null ? null : t.getTitle(), p.getTitle());
        this.subjectArea = pick(t == null ? null : t.getSubjectArea(), p.getSubjectArea());
        this.description = pick(t == null ? null : t.getDescription(), p.getDescription());
        this.content = pick(t == null ? null : t.getContent(), p.getContent());
        this.participatingUniversities = pick(t == null ? null : t.getParticipatingUniversities(), p.getParticipatingUniversities());
        this.startYear = p.getStartYear();
        this.endYear = p.getEndYear();
        this.status = p.getStatus();
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
