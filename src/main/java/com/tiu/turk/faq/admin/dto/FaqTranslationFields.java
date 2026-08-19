package com.tiu.turk.faq.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a FAQ, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * FaqTranslationEntity (faq_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class FaqTranslationFields {
    private String question;
    private String answer;
}
