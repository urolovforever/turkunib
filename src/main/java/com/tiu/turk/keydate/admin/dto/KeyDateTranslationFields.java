package com.tiu.turk.keydate.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a key date, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * KeyDateTranslationEntity (key_date_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class KeyDateTranslationFields {
    private String title;
    private String description;
}
