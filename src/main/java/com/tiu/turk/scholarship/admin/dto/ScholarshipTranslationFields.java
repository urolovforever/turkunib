package com.tiu.turk.scholarship.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a scholarship, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * ScholarshipTranslationEntity (scholarship_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class ScholarshipTranslationFields {
    private String title;
    private String provider;
    private String description;
    private String note;
    private String content;
    private String coverage;
    private String eligibility;
    private String amount;
}
