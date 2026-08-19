package com.tiu.turk.webinar.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import com.tiu.turk.webinar.admin.dto.WebinarTranslationFields;
import com.tiu.turk.webinar.admin.dto.WebinarTranslationsForm;
import com.tiu.turk.webinar.common.entity.WebinarEntity;
import com.tiu.turk.webinar.common.entity.WebinarRegistrationEntity;
import com.tiu.turk.webinar.common.entity.WebinarTranslationEntity;
import com.tiu.turk.webinar.common.repository.WebinarRegistrationRepository;
import com.tiu.turk.webinar.common.repository.WebinarRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebinarAdminService {
    private final WebinarRepository webinarRepository;
    private final WebinarRegistrationRepository registrationRepository;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        WebinarEntity w = this.getById(id);
        String sourceText = Stream.of(w.getTitle(), w.getDescription(), w.getContent())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializeWebinarTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<WebinarEntity> getAll() {
        return this.webinarRepository.findAllByOrderByStartAtDesc();
    }

    public WebinarEntity getById(Long id) {
        return this.webinarRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Webinar not found: " + id));
    }

    @Transactional
    public void update(Long id, String title, String description, String content, LocalDateTime startAt,
                       Integer durationMinutes, String onlineLink, Integer capacity) {
        WebinarEntity webinar = this.getById(id);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (startAt == null) {
            throw new IllegalArgumentException("Start date/time is required.");
        }
        webinar.setTitle(title.trim());
        webinar.setDescription(description);
        webinar.setContent(content);
        webinar.setStartAt(startAt);
        webinar.setDurationMinutes(durationMinutes);
        webinar.setOnlineLink(onlineLink);
        webinar.setCapacity(capacity);
        this.webinarRepository.save(webinar);
    }

    public long registrationCount(Long webinarId) {
        return this.registrationRepository.countByWebinar_Id(webinarId);
    }

    public List<WebinarRegistrationEntity> getRegistrations(Long webinarId) {
        return this.registrationRepository.findByWebinar_IdOrderByCreatedAtDesc(webinarId);
    }

    @Transactional
    public void create(String title, String description, String content, LocalDateTime startAt, Integer durationMinutes,
                       String onlineLink, Integer capacity, Long authorId) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (startAt == null) {
            throw new IllegalArgumentException("Start date/time is required.");
        }
        WebinarEntity webinar = new WebinarEntity();
        webinar.setTitle(title.trim());
        webinar.setDescription(description);
        webinar.setContent(content);
        webinar.setStartAt(startAt);
        webinar.setDurationMinutes(durationMinutes);
        webinar.setOnlineLink(onlineLink);
        webinar.setCapacity(capacity);
        webinar.setEnabled(true);
        webinar.setAuthor(new UserEntity(authorId));
        this.webinarRepository.save(webinar);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public WebinarTranslationsForm emptyForm() {
        WebinarTranslationsForm form = new WebinarTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new WebinarTranslationFields());
        }
        return form;
    }

    /** Creates a webinar with all languages at once: EN → base entity, other locales → webinar_i18n. */
    @Transactional
    public void createWithTranslations(WebinarTranslationsForm form, LocalDateTime startAt, Integer durationMinutes,
                                       String onlineLink, Integer capacity, Long authorId) {
        WebinarTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getTitle() == null || en.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required.");
        }
        if (startAt == null) {
            throw new IllegalArgumentException("Start date/time is required.");
        }
        WebinarEntity webinar = new WebinarEntity();
        webinar.setTitle(en.getTitle().trim());
        webinar.setDescription(trimToNull(en.getDescription()));
        webinar.setContent(trimToNull(en.getContent()));
        webinar.setStartAt(startAt);
        webinar.setDurationMinutes(durationMinutes);
        webinar.setOnlineLink(onlineLink);
        webinar.setCapacity(capacity);
        webinar.setEnabled(true);
        webinar.setAuthor(new UserEntity(authorId));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            WebinarTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            WebinarTranslationEntity t = new WebinarTranslationEntity();
            t.setWebinar(webinar);
            t.setLocale(locale);
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
            t.setContent(trimToNull(f.getContent()));
            webinar.getTranslations().put(locale, t);
        }
        this.webinarRepository.save(webinar);
    }

    private static boolean isAllBlank(WebinarTranslationFields f) {
        return Stream.of(f.getTitle(), f.getDescription(), f.getContent())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from webinar_i18n. */
    @Transactional(readOnly = true)
    public WebinarTranslationsForm getTranslationsForm(Long id) {
        WebinarEntity w = this.webinarRepository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Webinar not found: " + id));
        WebinarTranslationsForm form = new WebinarTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            WebinarTranslationFields f = new WebinarTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setTitle(w.getTitle());
                f.setDescription(w.getDescription());
                f.setContent(w.getContent());
            } else {
                WebinarTranslationEntity t = w.getTranslations().get(locale);
                if (t != null) {
                    f.setTitle(t.getTitle());
                    f.setDescription(t.getDescription());
                    f.setContent(t.getContent());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the webinar_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, WebinarTranslationsForm form) {
        WebinarEntity w = this.webinarRepository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Webinar not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            WebinarTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            WebinarTranslationEntity t = w.getTranslations().get(locale);
            if (t == null) {
                t = new WebinarTranslationEntity();
                t.setWebinar(w);
                t.setLocale(locale);
                w.getTranslations().put(locale, t);
            }
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
            t.setContent(trimToNull(f.getContent()));
        }
        this.webinarRepository.save(w);
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
        this.webinarRepository.deleteById(id);
    }
}
