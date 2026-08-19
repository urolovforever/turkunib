package com.tiu.turk.scholarship.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
import com.tiu.turk.scholarship.web.dto.ScholarshipView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScholarshipWebService {
    private final ScholarshipRepository repository;

    @Transactional(readOnly = true)
    public List<ScholarshipView> getScholarships(TranslationLocale locale) {
        return this.repository.findEnabledWithTranslations().stream()
                .map(s -> new ScholarshipView(s, locale))
                .toList();
    }

    @Transactional(readOnly = true)
    public ScholarshipView getById(TranslationLocale locale, Long id) {
        ScholarshipEntity s = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Scholarship not found with ID: " + id));
        return new ScholarshipView(s, locale);
    }
}
