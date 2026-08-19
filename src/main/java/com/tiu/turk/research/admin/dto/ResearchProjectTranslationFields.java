package com.tiu.turk.research.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a research project, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * ResearchProjectTranslationEntity (research_project_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class ResearchProjectTranslationFields {
    private String title;
    private String subjectArea;
    private String description;
    private String content;
    private String participatingUniversities;
}
