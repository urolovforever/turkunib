package com.tiu.turk.faq.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.faq.common.FaqCategory;
import com.tiu.turk.faq.common.entity.FaqEntity;
import com.tiu.turk.faq.common.entity.FaqTranslationEntity;
import lombok.Getter;

/**
 * Locale-aware read view of an FAQ. Translatable text fields (question, answer) resolve
 * to the requested locale's translation when present (and non-blank), otherwise fall back
 * to the base English text stored on the entity. Non-translatable fields (id, category,
 * displayOrder) pass through from the base entity.
 */
@Getter
public class FaqView {
    private final Long id;
    private final String question;
    private final String answer;
    private final FaqCategory category;
    private final Integer displayOrder;

    public FaqView(FaqEntity f, TranslationLocale locale) {
        FaqTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : f.getTranslations().get(locale);
        this.id = f.getId();
        this.question = pick(t == null ? null : t.getQuestion(), f.getQuestion());
        this.answer = pick(t == null ? null : t.getAnswer(), f.getAnswer());
        this.category = f.getCategory();
        this.displayOrder = f.getDisplayOrder();
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
