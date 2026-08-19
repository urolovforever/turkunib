package com.tiu.turk.institutionaldoc.admin.dto;

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
public class InstitutionalDocumentTranslationsForm {
    private Map<String, InstitutionalDocumentTranslationFields> translations = new HashMap<>();
}
