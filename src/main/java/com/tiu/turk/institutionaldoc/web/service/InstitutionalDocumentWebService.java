package com.tiu.turk.institutionaldoc.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.institutionaldoc.common.InstitutionalDocumentSection;
import com.tiu.turk.institutionaldoc.common.repository.InstitutionalDocumentRepository;
import com.tiu.turk.institutionaldoc.web.dto.InstitutionalDocumentView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstitutionalDocumentWebService {
    private final InstitutionalDocumentRepository repository;

    @Transactional(readOnly = true)
    public List<InstitutionalDocumentView> getBySection(InstitutionalDocumentSection section, TranslationLocale locale) {
        return this.repository.findBySectionWithTranslations(section).stream()
                .map(d -> new InstitutionalDocumentView(d, locale))
                .toList();
    }
}
