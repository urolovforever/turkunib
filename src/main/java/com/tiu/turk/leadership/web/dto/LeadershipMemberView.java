package com.tiu.turk.leadership.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.leadership.common.entity.LeadershipMemberEntity;
import com.tiu.turk.leadership.common.entity.LeadershipMemberTranslationEntity;
import lombok.Getter;

/**
 * Locale-aware read view of a leadership member. Translatable text fields (position,
 * organization, bio) resolve to the requested locale's translation when present (and
 * non-blank), otherwise fall back to the base English text stored on the entity.
 * Non-translatable fields (id, fullName, photo, displayOrder) pass through from the
 * base entity. fullName is a proper name and is never translated.
 */
@Getter
public class LeadershipMemberView {
    private final Long id;
    private final String fullName;
    private final String position;
    private final String organization;
    private final ImageEntity photo;
    private final String bio;
    private final Integer displayOrder;

    public LeadershipMemberView(LeadershipMemberEntity m, TranslationLocale locale) {
        LeadershipMemberTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : m.getTranslations().get(locale);
        this.id = m.getId();
        this.fullName = m.getFullName();
        this.position = pick(t == null ? null : t.getPosition(), m.getPosition());
        this.organization = pick(t == null ? null : t.getOrganization(), m.getOrganization());
        this.photo = m.getPhoto();
        this.bio = pick(t == null ? null : t.getBio(), m.getBio());
        this.displayOrder = m.getDisplayOrder();
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
