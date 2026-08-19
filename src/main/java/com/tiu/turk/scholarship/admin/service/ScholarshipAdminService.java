package com.tiu.turk.scholarship.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileService;
import com.tiu.turk.scholarship.admin.dto.ScholarshipTranslationFields;
import com.tiu.turk.scholarship.admin.dto.ScholarshipTranslationsForm;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.scholarship.common.entity.ScholarshipTranslationEntity;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ScholarshipAdminService {
    private final ScholarshipRepository repository;
    private final FileService fileService;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        ScholarshipEntity s = this.getById(id);
        String sourceText = Stream.of(s.getTitle(), s.getProvider(), s.getDescription(), s.getContent(),
                        s.getCoverage(), s.getEligibility(), s.getAmount())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializeScholarshipTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<ScholarshipEntity> getAll() {
        return this.repository.findAllByOrderByDisplayOrderAscIdAsc();
    }

    public ScholarshipEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Scholarship not found: " + id));
    }

    /**
     * Orhun round update. A round only carries: title, announcement description,
     * note (participating countries), the application period (start–deadline) and
     * the timeline dates. The remaining legacy scholarship columns are unused.
     */
    @Transactional
    public void update(Long id, String title, String description, String note,
                       LocalDate applicationStart, LocalDate deadline, Integer displayOrder,
                       com.tiu.turk.scholarship.admin.dto.RoundDatesForm dates, Long authorId) {
        ScholarshipEntity scholarship = this.getById(id);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        scholarship.setTitle(title.trim());
        scholarship.setDescription(trimToNull(description));
        scholarship.setNote(trimToNull(note));
        scholarship.setApplicationStart(applicationStart);
        scholarship.setDeadline(deadline);
        scholarship.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        if (dates != null) {
            scholarship.setTimeline1Start(dates.getTimeline1Start());
            scholarship.setTimeline1End(dates.getTimeline1End());
            scholarship.setTimeline2Start(dates.getTimeline2Start());
            scholarship.setTimeline2End(dates.getTimeline2End());
            scholarship.setTimeline3Start(dates.getTimeline3Start());
            scholarship.setTimeline3End(dates.getTimeline3End());
            scholarship.setTimeline4Start(dates.getTimeline4Start());
            scholarship.setTimeline4End(dates.getTimeline4End());
            scholarship.setTimeline5Date(dates.getTimeline5Date());
        }
        this.repository.save(scholarship);
    }

    @Transactional
    public void create(String title, String provider, String description, String content, String coverage, String eligibility,
                       String amount, LocalDate deadline, String applyUrl, Integer displayOrder,
                       MultipartFile document, Long authorId) throws IOException {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        ScholarshipEntity scholarship = new ScholarshipEntity();
        scholarship.setTitle(title.trim());
        scholarship.setProvider(provider);
        scholarship.setDescription(description);
        scholarship.setContent(content);
        scholarship.setCoverage(coverage);
        scholarship.setEligibility(eligibility);
        scholarship.setAmount(amount);
        scholarship.setDeadline(deadline);
        scholarship.setApplyUrl(normalizeUrl(applyUrl));
        scholarship.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        scholarship.setEnabled(true);
        scholarship.setAuthor(new UserEntity(authorId));
        if (document != null && !document.isEmpty()) {
            FileEntity stored = this.fileService.store(document, document.getOriginalFilename(), "scholarship", authorId);
            scholarship.setDocument(stored);
        }
        this.repository.save(scholarship);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public ScholarshipTranslationsForm emptyForm() {
        ScholarshipTranslationsForm form = new ScholarshipTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new ScholarshipTranslationFields());
        }
        return form;
    }

    /** Creates a scholarship with all languages at once: EN → base entity, other locales → scholarship_i18n. */
    @Transactional
    public void createWithTranslations(ScholarshipTranslationsForm form, LocalDate applicationStart, LocalDate deadline, String applyUrl,
                                       Integer displayOrder, MultipartFile document, Long authorId) throws IOException {
        ScholarshipTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getTitle() == null || en.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required.");
        }
        ScholarshipEntity scholarship = new ScholarshipEntity();
        scholarship.setTitle(en.getTitle().trim());
        scholarship.setProvider(trimToNull(en.getProvider()));
        scholarship.setDescription(trimToNull(en.getDescription()));
        scholarship.setContent(trimToNull(en.getContent()));
        scholarship.setCoverage(trimToNull(en.getCoverage()));
        scholarship.setEligibility(trimToNull(en.getEligibility()));
        scholarship.setAmount(trimToNull(en.getAmount()));
        scholarship.setNote(trimToNull(en.getNote()));
        scholarship.setApplicationStart(applicationStart);
        scholarship.setDeadline(deadline);
        scholarship.setApplyUrl(normalizeUrl(applyUrl));
        scholarship.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        scholarship.setEnabled(true);
        scholarship.setAuthor(new UserEntity(authorId));
        if (document != null && !document.isEmpty()) {
            FileEntity stored = this.fileService.store(document, document.getOriginalFilename(), "scholarship", authorId);
            scholarship.setDocument(stored);
        }
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            ScholarshipTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            ScholarshipTranslationEntity t = new ScholarshipTranslationEntity();
            t.setScholarship(scholarship);
            t.setLocale(locale);
            t.setTitle(trimToNull(f.getTitle()));
            t.setProvider(trimToNull(f.getProvider()));
            t.setDescription(trimToNull(f.getDescription()));
            t.setContent(trimToNull(f.getContent()));
            t.setCoverage(trimToNull(f.getCoverage()));
            t.setEligibility(trimToNull(f.getEligibility()));
            t.setAmount(trimToNull(f.getAmount()));
            t.setNote(trimToNull(f.getNote()));
            scholarship.getTranslations().put(locale, t);
        }
        this.repository.save(scholarship);
    }

    private static boolean isAllBlank(ScholarshipTranslationFields f) {
        return Stream.of(f.getTitle(), f.getProvider(), f.getDescription(), f.getContent(),
                        f.getCoverage(), f.getEligibility(), f.getAmount(), f.getNote())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from scholarship_i18n. */
    @Transactional(readOnly = true)
    public ScholarshipTranslationsForm getTranslationsForm(Long id) {
        ScholarshipEntity s = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Scholarship not found: " + id));
        ScholarshipTranslationsForm form = new ScholarshipTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            ScholarshipTranslationFields f = new ScholarshipTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setTitle(s.getTitle());
                f.setProvider(s.getProvider());
                f.setDescription(s.getDescription());
                f.setContent(s.getContent());
                f.setCoverage(s.getCoverage());
                f.setEligibility(s.getEligibility());
                f.setAmount(s.getAmount());
                f.setNote(s.getNote());
            } else {
                ScholarshipTranslationEntity t = s.getTranslations().get(locale);
                if (t != null) {
                    f.setTitle(t.getTitle());
                    f.setProvider(t.getProvider());
                    f.setDescription(t.getDescription());
                    f.setContent(t.getContent());
                    f.setCoverage(t.getCoverage());
                    f.setEligibility(t.getEligibility());
                    f.setAmount(t.getAmount());
                    f.setNote(t.getNote());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the scholarship_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, ScholarshipTranslationsForm form) {
        ScholarshipEntity s = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Scholarship not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            ScholarshipTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            ScholarshipTranslationEntity t = s.getTranslations().get(locale);
            if (t == null) {
                t = new ScholarshipTranslationEntity();
                t.setScholarship(s);
                t.setLocale(locale);
                s.getTranslations().put(locale, t);
            }
            t.setTitle(trimToNull(f.getTitle()));
            t.setProvider(trimToNull(f.getProvider()));
            t.setDescription(trimToNull(f.getDescription()));
            t.setContent(trimToNull(f.getContent()));
            t.setCoverage(trimToNull(f.getCoverage()));
            t.setEligibility(trimToNull(f.getEligibility()));
            t.setAmount(trimToNull(f.getAmount()));
            t.setNote(trimToNull(f.getNote()));
        }
        this.repository.save(s);
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

    private String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        String u = url.trim();
        if (!u.matches("(?i)^https?://.*")) {
            u = "https://" + u;
        }
        return u;
    }
}
