package com.tiu.turk.research.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.research.common.ResearchProjectStatus;
import com.tiu.turk.research.common.entity.ResearchProjectEntity;
import com.tiu.turk.research.common.repository.ResearchProjectRepository;
import com.tiu.turk.research.web.dto.ResearchProjectView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResearchProjectWebService {
    private final ResearchProjectRepository repository;

    @Transactional(readOnly = true)
    public List<ResearchProjectView> getProjects(ResearchProjectStatus status, TranslationLocale locale) {
        List<ResearchProjectEntity> projects = (status != null)
                ? this.repository.findEnabledByStatusWithTranslations(status)
                : this.repository.findEnabledWithTranslations();
        return projects.stream()
                .map(p -> new ResearchProjectView(p, locale))
                .toList();
    }

    @Transactional(readOnly = true)
    public ResearchProjectView getById(TranslationLocale locale, Long id) {
        ResearchProjectEntity p = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Research project not found with ID: " + id));
        return new ResearchProjectView(p, locale);
    }
}
