package com.tiu.turk.publication.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Editable per-locale translation fields for a publication, used as a form-backing
 * object on the admin "Languages" editor. Mirrors the translatable columns of
 * PublicationTranslationEntity (publication_i18n).
 */
@Getter
@Setter
@NoArgsConstructor
public class PublicationTranslationFields {
    private String title;
    private String description;
}
