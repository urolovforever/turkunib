package com.tiu.turk.institutionaldoc.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileService;
import com.tiu.turk.institutionaldoc.admin.dto.InstitutionalDocumentTranslationFields;
import com.tiu.turk.institutionaldoc.admin.dto.InstitutionalDocumentTranslationsForm;
import com.tiu.turk.institutionaldoc.common.InstitutionalDocumentSection;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentEntity;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentTranslationEntity;
import com.tiu.turk.institutionaldoc.common.repository.InstitutionalDocumentRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class InstitutionalDocumentAdminService {
    private final InstitutionalDocumentRepository repository;
    private final FileService fileService;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        InstitutionalDocumentEntity d = this.getById(id);
        String sourceText = Stream.of(d.getTitle(), d.getDescription())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializeInstitutionalDocumentTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<InstitutionalDocumentEntity> getAll() {
        return this.repository.findAllByOrderByCreatedAtDesc();
    }

    public InstitutionalDocumentEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
    }

    @Transactional
    public void update(Long id, InstitutionalDocumentSection section, String title, Integer year, String description,
                       MultipartFile file, Long authorId) throws IOException {
        InstitutionalDocumentEntity document = this.getById(id);
        if (section == null) {
            throw new IllegalArgumentException("Section is required.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        document.setSection(section);
        document.setTitle(title.trim());
        document.setDocumentYear(year);
        document.setDescription(description);
        if (file != null && !file.isEmpty()) {
            FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "institutional-document", authorId);
            document.setFile(stored);
        }
        this.repository.save(document);
    }

    @Transactional
    public void create(InstitutionalDocumentSection section, String title, Integer year, String description,
                       MultipartFile file, Long authorId) throws IOException {
        if (section == null) {
            throw new IllegalArgumentException("Section is required.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A file is required.");
        }
        FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "institutional-document", authorId);
        InstitutionalDocumentEntity document = new InstitutionalDocumentEntity();
        document.setSection(section);
        document.setTitle(title.trim());
        document.setDocumentYear(year);
        document.setDescription(description);
        document.setFile(stored);
        document.setEnabled(true);
        document.setAuthor(new UserEntity(authorId));
        this.repository.save(document);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public InstitutionalDocumentTranslationsForm emptyForm() {
        InstitutionalDocumentTranslationsForm form = new InstitutionalDocumentTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new InstitutionalDocumentTranslationFields());
        }
        return form;
    }

    /** Creates a document with all languages at once: EN → base entity, other locales → institutional_document_i18n. */
    @Transactional
    public void createWithTranslations(InstitutionalDocumentTranslationsForm form, InstitutionalDocumentSection section,
                                       Integer documentYear, MultipartFile file, Long authorId) throws IOException {
        if (section == null) {
            throw new IllegalArgumentException("Section is required.");
        }
        InstitutionalDocumentTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getTitle() == null || en.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required.");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A file is required.");
        }
        FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "institutional-document", authorId);
        InstitutionalDocumentEntity document = new InstitutionalDocumentEntity();
        document.setSection(section);
        document.setTitle(en.getTitle().trim());
        document.setDocumentYear(documentYear);
        document.setDescription(trimToNull(en.getDescription()));
        document.setFile(stored);
        document.setEnabled(true);
        document.setAuthor(new UserEntity(authorId));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            InstitutionalDocumentTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            InstitutionalDocumentTranslationEntity t = new InstitutionalDocumentTranslationEntity();
            t.setInstitutionalDocument(document);
            t.setLocale(locale);
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
            document.getTranslations().put(locale, t);
        }
        this.repository.save(document);
    }

    private static boolean isAllBlank(InstitutionalDocumentTranslationFields f) {
        return Stream.of(f.getTitle(), f.getDescription())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from institutional_document_i18n. */
    @Transactional(readOnly = true)
    public InstitutionalDocumentTranslationsForm getTranslationsForm(Long id) {
        InstitutionalDocumentEntity d = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
        InstitutionalDocumentTranslationsForm form = new InstitutionalDocumentTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            InstitutionalDocumentTranslationFields f = new InstitutionalDocumentTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setTitle(d.getTitle());
                f.setDescription(d.getDescription());
            } else {
                InstitutionalDocumentTranslationEntity t = d.getTranslations().get(locale);
                if (t != null) {
                    f.setTitle(t.getTitle());
                    f.setDescription(t.getDescription());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the institutional_document_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, InstitutionalDocumentTranslationsForm form) {
        InstitutionalDocumentEntity d = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            InstitutionalDocumentTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            InstitutionalDocumentTranslationEntity t = d.getTranslations().get(locale);
            if (t == null) {
                t = new InstitutionalDocumentTranslationEntity();
                t.setInstitutionalDocument(d);
                t.setLocale(locale);
                d.getTranslations().put(locale, t);
            }
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
        }
        this.repository.save(d);
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
