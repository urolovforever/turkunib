package com.tiu.turk.research.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.research.admin.dto.ResearchProjectTranslationFields;
import com.tiu.turk.research.admin.dto.ResearchProjectTranslationsForm;
import com.tiu.turk.research.common.ResearchProjectStatus;
import com.tiu.turk.research.common.entity.ResearchProjectEntity;
import com.tiu.turk.research.common.entity.ResearchProjectTranslationEntity;
import com.tiu.turk.research.common.repository.ResearchProjectRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResearchProjectAdminService {
    private final ResearchProjectRepository repository;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        ResearchProjectEntity p = this.getById(id);
        String sourceText = Stream.of(p.getTitle(), p.getSubjectArea(), p.getDescription(), p.getContent(),
                        p.getParticipatingUniversities())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializeResearchProjectTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<ResearchProjectEntity> getAll() {
        return this.repository.findAllByOrderByCreatedAtDesc();
    }

    public ResearchProjectEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    @Transactional
    public void update(Long id, String title, String subjectArea, String description, String content, String participatingUniversities,
                       Integer startYear, Integer endYear, ResearchProjectStatus status) {
        ResearchProjectEntity project = this.getById(id);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        project.setTitle(title.trim());
        project.setSubjectArea(subjectArea);
        project.setDescription(description);
        project.setContent(content);
        project.setParticipatingUniversities(participatingUniversities);
        project.setStartYear(startYear);
        project.setEndYear(endYear);
        project.setStatus(status != null ? status : ResearchProjectStatus.ONGOING);
        this.repository.save(project);
    }

    @Transactional
    public void create(String title, String subjectArea, String description, String content, String participatingUniversities,
                       Integer startYear, Integer endYear, ResearchProjectStatus status, Long authorId) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        ResearchProjectEntity project = new ResearchProjectEntity();
        project.setTitle(title.trim());
        project.setSubjectArea(subjectArea);
        project.setDescription(description);
        project.setContent(content);
        project.setParticipatingUniversities(participatingUniversities);
        project.setStartYear(startYear);
        project.setEndYear(endYear);
        project.setStatus(status != null ? status : ResearchProjectStatus.ONGOING);
        project.setEnabled(true);
        project.setAuthor(new UserEntity(authorId));
        this.repository.save(project);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public ResearchProjectTranslationsForm emptyForm() {
        ResearchProjectTranslationsForm form = new ResearchProjectTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new ResearchProjectTranslationFields());
        }
        return form;
    }

    /** Creates a research project with all languages at once: EN → base entity, other locales → research_project_i18n. */
    @Transactional
    public void createWithTranslations(ResearchProjectTranslationsForm form, Integer startYear, Integer endYear,
                                       ResearchProjectStatus status, Long authorId) {
        ResearchProjectTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getTitle() == null || en.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required.");
        }
        ResearchProjectEntity project = new ResearchProjectEntity();
        project.setTitle(en.getTitle().trim());
        project.setSubjectArea(trimToNull(en.getSubjectArea()));
        project.setDescription(trimToNull(en.getDescription()));
        project.setContent(trimToNull(en.getContent()));
        project.setParticipatingUniversities(trimToNull(en.getParticipatingUniversities()));
        project.setStartYear(startYear);
        project.setEndYear(endYear);
        project.setStatus(status != null ? status : ResearchProjectStatus.ONGOING);
        project.setEnabled(true);
        project.setAuthor(new UserEntity(authorId));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            ResearchProjectTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            ResearchProjectTranslationEntity t = new ResearchProjectTranslationEntity();
            t.setResearchProject(project);
            t.setLocale(locale);
            t.setTitle(trimToNull(f.getTitle()));
            t.setSubjectArea(trimToNull(f.getSubjectArea()));
            t.setDescription(trimToNull(f.getDescription()));
            t.setContent(trimToNull(f.getContent()));
            t.setParticipatingUniversities(trimToNull(f.getParticipatingUniversities()));
            project.getTranslations().put(locale, t);
        }
        this.repository.save(project);
    }

    private static boolean isAllBlank(ResearchProjectTranslationFields f) {
        return Stream.of(f.getTitle(), f.getSubjectArea(), f.getDescription(), f.getContent(),
                        f.getParticipatingUniversities())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from research_project_i18n. */
    @Transactional(readOnly = true)
    public ResearchProjectTranslationsForm getTranslationsForm(Long id) {
        ResearchProjectEntity p = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        ResearchProjectTranslationsForm form = new ResearchProjectTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            ResearchProjectTranslationFields f = new ResearchProjectTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setTitle(p.getTitle());
                f.setSubjectArea(p.getSubjectArea());
                f.setDescription(p.getDescription());
                f.setContent(p.getContent());
                f.setParticipatingUniversities(p.getParticipatingUniversities());
            } else {
                ResearchProjectTranslationEntity t = p.getTranslations().get(locale);
                if (t != null) {
                    f.setTitle(t.getTitle());
                    f.setSubjectArea(t.getSubjectArea());
                    f.setDescription(t.getDescription());
                    f.setContent(t.getContent());
                    f.setParticipatingUniversities(t.getParticipatingUniversities());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the research_project_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, ResearchProjectTranslationsForm form) {
        ResearchProjectEntity p = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            ResearchProjectTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            ResearchProjectTranslationEntity t = p.getTranslations().get(locale);
            if (t == null) {
                t = new ResearchProjectTranslationEntity();
                t.setResearchProject(p);
                t.setLocale(locale);
                p.getTranslations().put(locale, t);
            }
            t.setTitle(trimToNull(f.getTitle()));
            t.setSubjectArea(trimToNull(f.getSubjectArea()));
            t.setDescription(trimToNull(f.getDescription()));
            t.setContent(trimToNull(f.getContent()));
            t.setParticipatingUniversities(trimToNull(f.getParticipatingUniversities()));
        }
        this.repository.save(p);
    }

    private static String trimToNull(String v) {
        if (v == null) {
            return null;
        }
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }

    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}
