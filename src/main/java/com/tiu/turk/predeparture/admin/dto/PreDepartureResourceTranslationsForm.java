package com.tiu.turk.predeparture.admin.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Form-backing object for the admin "Languages" editor. The map is keyed by
 * TranslationLocale name (EN, KG, KZ, TR, UZ, HG, AZ). Spring auto-grows missing
 * entries during binding, so th:field="*{translations['UZ'].title}" works directly.
 */
@Getter
@Setter
public class PreDepartureResourceTranslationsForm {
    private Map<String, PreDepartureResourceTranslationFields> translations = new HashMap<>();
}
