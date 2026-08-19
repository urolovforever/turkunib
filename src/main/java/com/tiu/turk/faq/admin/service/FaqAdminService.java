package com.tiu.turk.faq.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.faq.admin.dto.FaqTranslationFields;
import com.tiu.turk.faq.admin.dto.FaqTranslationsForm;
import com.tiu.turk.faq.common.FaqCategory;
import com.tiu.turk.faq.common.entity.FaqEntity;
import com.tiu.turk.faq.common.entity.FaqTranslationEntity;
import com.tiu.turk.faq.common.repository.FaqRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaqAdminService {
    private final FaqRepository repository;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        FaqEntity f = this.getById(id);
        String sourceText = Stream.of(f.getQuestion(), f.getAnswer())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializeFaqTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<FaqEntity> getAll() {
        return this.repository.findAllByOrderByCategoryAscDisplayOrderAscIdAsc();
    }

    public FaqEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + id));
    }

    @Transactional
    public void update(Long id, String question, String answer, FaqCategory category, Integer displayOrder) {
        FaqEntity faq = this.getById(id);
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question is required.");
        }
        faq.setQuestion(question.trim());
        faq.setAnswer(answer);
        faq.setCategory(category != null ? category : FaqCategory.GENERAL);
        faq.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        this.repository.save(faq);
    }

    @Transactional
    public void create(String question, String answer, FaqCategory category, Integer displayOrder, Long authorId) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question is required.");
        }
        FaqEntity faq = new FaqEntity();
        faq.setQuestion(question.trim());
        faq.setAnswer(answer);
        faq.setCategory(category != null ? category : FaqCategory.GENERAL);
        faq.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        faq.setEnabled(true);
        faq.setAuthor(new UserEntity(authorId));
        this.repository.save(faq);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public FaqTranslationsForm emptyForm() {
        FaqTranslationsForm form = new FaqTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new FaqTranslationFields());
        }
        return form;
    }

    /** Creates a FAQ with all languages at once: EN → base entity, other locales → faq_i18n. */
    @Transactional
    public void createWithTranslations(FaqTranslationsForm form, FaqCategory category, Integer displayOrder, Long authorId) {
        FaqTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getQuestion() == null || en.getQuestion().isBlank()) {
            throw new IllegalArgumentException("English question is required.");
        }
        FaqEntity faq = new FaqEntity();
        faq.setQuestion(en.getQuestion().trim());
        faq.setAnswer(trimToNull(en.getAnswer()));
        faq.setCategory(category != null ? category : FaqCategory.GENERAL);
        faq.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        faq.setEnabled(true);
        faq.setAuthor(new UserEntity(authorId));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            FaqTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            FaqTranslationEntity t = new FaqTranslationEntity();
            t.setFaq(faq);
            t.setLocale(locale);
            t.setQuestion(trimToNull(f.getQuestion()));
            t.setAnswer(trimToNull(f.getAnswer()));
            faq.getTranslations().put(locale, t);
        }
        this.repository.save(faq);
    }

    private static boolean isAllBlank(FaqTranslationFields f) {
        return Stream.of(f.getQuestion(), f.getAnswer())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from faq_i18n. */
    @Transactional(readOnly = true)
    public FaqTranslationsForm getTranslationsForm(Long id) {
        FaqEntity faq = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + id));
        FaqTranslationsForm form = new FaqTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            FaqTranslationFields fields = new FaqTranslationFields();
            if (locale == TranslationLocale.EN) {
                fields.setQuestion(faq.getQuestion());
                fields.setAnswer(faq.getAnswer());
            } else {
                FaqTranslationEntity t = faq.getTranslations().get(locale);
                if (t != null) {
                    fields.setQuestion(t.getQuestion());
                    fields.setAnswer(t.getAnswer());
                }
            }
            form.getTranslations().put(locale.name(), fields);
        }
        return form;
    }

    /** Upserts the faq_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, FaqTranslationsForm form) {
        FaqEntity faq = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            FaqTranslationFields fields = form.getTranslations().get(locale.name());
            if (fields == null) {
                continue;
            }
            FaqTranslationEntity t = faq.getTranslations().get(locale);
            if (t == null) {
                t = new FaqTranslationEntity();
                t.setFaq(faq);
                t.setLocale(locale);
                faq.getTranslations().put(locale, t);
            }
            t.setQuestion(trimToNull(fields.getQuestion()));
            t.setAnswer(trimToNull(fields.getAnswer()));
        }
        this.repository.save(faq);
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
