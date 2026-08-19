package com.tiu.turk.webinar.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a webinar, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * WebinarTranslationEntity (webinar_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class WebinarTranslationFields {
    private String title;
    private String description;
    private String content;
}
