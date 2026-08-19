package com.tiu.turk.keydate.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.keydate.admin.dto.KeyDateTranslationFields;
import com.tiu.turk.keydate.admin.dto.KeyDateTranslationsForm;
import com.tiu.turk.keydate.common.KeyDateType;
import com.tiu.turk.keydate.common.entity.KeyDateEntity;
import com.tiu.turk.keydate.common.entity.KeyDateTranslationEntity;
import com.tiu.turk.keydate.common.repository.KeyDateRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeyDateAdminService {
    private final KeyDateRepository repository;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        KeyDateEntity k = this.getById(id);
        String sourceText = Stream.of(k.getTitle(), k.getDescription())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializeKeyDateTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<KeyDateEntity> getAll() {
        return this.repository.findAllByOrderByEventDateAsc();
    }

    public KeyDateEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Date not found: " + id));
    }

    @Transactional
    public void update(Long id, String title, LocalDate eventDate, LocalDate endDate, String description, KeyDateType type) {
        KeyDateEntity keyDate = this.getById(id);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (eventDate == null) {
            throw new IllegalArgumentException("Date is required.");
        }
        keyDate.setTitle(title.trim());
        keyDate.setEventDate(eventDate);
        keyDate.setEndDate(endDate);
        keyDate.setDescription(description);
        keyDate.setType(type != null ? type : KeyDateType.EVENT);
        this.repository.save(keyDate);
    }

    @Transactional
    public void create(String title, LocalDate eventDate, LocalDate endDate, String description, KeyDateType type, Long authorId) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (eventDate == null) {
            throw new IllegalArgumentException("Date is required.");
        }
        KeyDateEntity keyDate = new KeyDateEntity();
        keyDate.setTitle(title.trim());
        keyDate.setEventDate(eventDate);
        keyDate.setEndDate(endDate);
        keyDate.setDescription(description);
        keyDate.setType(type != null ? type : KeyDateType.EVENT);
        keyDate.setEnabled(true);
        keyDate.setAuthor(new UserEntity(authorId));
        this.repository.save(keyDate);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public KeyDateTranslationsForm emptyForm() {
        KeyDateTranslationsForm form = new KeyDateTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new KeyDateTranslationFields());
        }
        return form;
    }

    /** Creates a key date with all languages at once: EN → base entity, other locales → key_date_i18n. */
    @Transactional
    public void createWithTranslations(KeyDateTranslationsForm form, LocalDate eventDate, LocalDate endDate,
                                       KeyDateType type, Long authorId) {
        KeyDateTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getTitle() == null || en.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required.");
        }
        if (eventDate == null) {
            throw new IllegalArgumentException("Date is required.");
        }
        KeyDateEntity keyDate = new KeyDateEntity();
        keyDate.setTitle(en.getTitle().trim());
        keyDate.setDescription(trimToNull(en.getDescription()));
        keyDate.setEventDate(eventDate);
        keyDate.setEndDate(endDate);
        keyDate.setType(type != null ? type : KeyDateType.EVENT);
        keyDate.setEnabled(true);
        keyDate.setAuthor(new UserEntity(authorId));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            KeyDateTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            KeyDateTranslationEntity t = new KeyDateTranslationEntity();
            t.setKeyDate(keyDate);
            t.setLocale(locale);
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
            keyDate.getTranslations().put(locale, t);
        }
        this.repository.save(keyDate);
    }

    private static boolean isAllBlank(KeyDateTranslationFields f) {
        return Stream.of(f.getTitle(), f.getDescription())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from key_date_i18n. */
    @Transactional(readOnly = true)
    public KeyDateTranslationsForm getTranslationsForm(Long id) {
        KeyDateEntity k = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Date not found: " + id));
        KeyDateTranslationsForm form = new KeyDateTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            KeyDateTranslationFields f = new KeyDateTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setTitle(k.getTitle());
                f.setDescription(k.getDescription());
            } else {
                KeyDateTranslationEntity t = k.getTranslations().get(locale);
                if (t != null) {
                    f.setTitle(t.getTitle());
                    f.setDescription(t.getDescription());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the key_date_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, KeyDateTranslationsForm form) {
        KeyDateEntity k = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Date not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            KeyDateTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            KeyDateTranslationEntity t = k.getTranslations().get(locale);
            if (t == null) {
                t = new KeyDateTranslationEntity();
                t.setKeyDate(k);
                t.setLocale(locale);
                k.getTranslations().put(locale, t);
            }
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
        }
        this.repository.save(k);
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
