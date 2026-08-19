package com.tiu.turk.leadership.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a leadership member, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * LeadershipMemberTranslationEntity (leadership_member_i18n).
 *
 * Note: fullName is a proper name and is NOT translatable, so it is intentionally absent.
 */
@Getter
@Setter
@NoArgsConstructor
public class LeadershipMemberTranslationFields {
    private String position;
    private String organization;
    private String bio;
}
