package com.tiu.turk.institutionaldoc.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for an institutional document, used as a
 * form-backing object on the admin "Languages" editor. Mirrors the translatable columns
 * of InstitutionalDocumentTranslationEntity (institutional_document_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class InstitutionalDocumentTranslationFields {
    private String title;
    private String description;
}
