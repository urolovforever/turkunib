package com.tiu.turk.predeparture.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a pre-departure resource, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * PreDepartureResourceTranslationEntity (pre_departure_resource_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class PreDepartureResourceTranslationFields {
    private String title;
    private String description;
}
