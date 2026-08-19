package com.tiu.turk.publication.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.publication.common.PublicationCategory;
import com.tiu.turk.publication.common.repository.PublicationRepository;
import com.tiu.turk.publication.web.dto.PublicationView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicationWebService {
    private final PublicationRepository repository;

    @Transactional(readOnly = true)
    public List<PublicationView> getPublications(PublicationCategory category, TranslationLocale locale) {
        List<? extends com.tiu.turk.publication.common.entity.PublicationEntity> entities =
                (category != null)
                        ? this.repository.findEnabledByCategoryWithTranslations(category)
                        : this.repository.findEnabledWithTranslations();
        return entities.stream()
                .map(p -> new PublicationView(p, locale))
                .toList();
    }
}
