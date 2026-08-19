package com.tiu.turk.publication.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileService;
import com.tiu.turk.publication.admin.dto.PublicationTranslationFields;
import com.tiu.turk.publication.admin.dto.PublicationTranslationsForm;
import com.tiu.turk.publication.common.PublicationCategory;
import com.tiu.turk.publication.common.entity.PublicationEntity;
import com.tiu.turk.publication.common.entity.PublicationTranslationEntity;
import com.tiu.turk.publication.common.repository.PublicationRepository;
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
public class PublicationAdminService {
    private final PublicationRepository repository;
    private final FileService fileService;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        PublicationEntity p = this.getById(id);
        String sourceText = Stream.of(p.getTitle(), p.getDescription())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializePublicationTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<PublicationEntity> getAll() {
        return this.repository.findAllByOrderByCreatedAtDesc();
    }

    public PublicationEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Publication not found: " + id));
    }

    @Transactional
    public void update(Long id, String title, String description, PublicationCategory category, Integer year, MultipartFile file, Long authorId) throws IOException {
        PublicationEntity publication = this.getById(id);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        publication.setTitle(title.trim());
        publication.setDescription(description);
        publication.setCategory(category != null ? category : PublicationCategory.ARTICLE);
        publication.setPublicationYear(year);
        if (file != null && !file.isEmpty()) {
            FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "publication", authorId);
            publication.setFile(stored);
        }
        this.repository.save(publication);
    }

    @Transactional
    public void create(String title, String description, PublicationCategory category, Integer year, MultipartFile file, Long authorId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A file is required.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "publication", authorId);
        PublicationEntity publication = new PublicationEntity();
        publication.setTitle(title.trim());
        publication.setDescription(description);
        publication.setCategory(category != null ? category : PublicationCategory.ARTICLE);
        publication.setPublicationYear(year);
        publication.setFile(stored);
        publication.setEnabled(true);
        publication.setAuthor(new UserEntity(authorId));
        this.repository.save(publication);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public PublicationTranslationsForm emptyForm() {
        PublicationTranslationsForm form = new PublicationTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new PublicationTranslationFields());
        }
        return form;
    }

    /** Creates a publication with all languages at once: EN → base entity, other locales → publication_i18n. */
    @Transactional
    public void createWithTranslations(PublicationTranslationsForm form, PublicationCategory category,
                                       Integer publicationYear, MultipartFile file, Long authorId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A file is required.");
        }
        PublicationTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getTitle() == null || en.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required.");
        }
        FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "publication", authorId);
        PublicationEntity publication = new PublicationEntity();
        publication.setTitle(en.getTitle().trim());
        publication.setDescription(trimToNull(en.getDescription()));
        publication.setCategory(category != null ? category : PublicationCategory.ARTICLE);
        publication.setPublicationYear(publicationYear);
        publication.setFile(stored);
        publication.setEnabled(true);
        publication.setAuthor(new UserEntity(authorId));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            PublicationTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            PublicationTranslationEntity t = new PublicationTranslationEntity();
            t.setPublication(publication);
            t.setLocale(locale);
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
            publication.getTranslations().put(locale, t);
        }
        this.repository.save(publication);
    }

    private static boolean isAllBlank(PublicationTranslationFields f) {
        return Stream.of(f.getTitle(), f.getDescription())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from publication_i18n. */
    @Transactional(readOnly = true)
    public PublicationTranslationsForm getTranslationsForm(Long id) {
        PublicationEntity p = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found: " + id));
        PublicationTranslationsForm form = new PublicationTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            PublicationTranslationFields f = new PublicationTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setTitle(p.getTitle());
                f.setDescription(p.getDescription());
            } else {
                PublicationTranslationEntity t = p.getTranslations().get(locale);
                if (t != null) {
                    f.setTitle(t.getTitle());
                    f.setDescription(t.getDescription());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the publication_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, PublicationTranslationsForm form) {
        PublicationEntity p = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            PublicationTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            PublicationTranslationEntity t = p.getTranslations().get(locale);
            if (t == null) {
                t = new PublicationTranslationEntity();
                t.setPublication(p);
                t.setLocale(locale);
                p.getTranslations().put(locale, t);
            }
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
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
