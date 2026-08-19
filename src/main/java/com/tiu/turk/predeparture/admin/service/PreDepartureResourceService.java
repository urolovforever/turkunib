package com.tiu.turk.predeparture.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileService;
import com.tiu.turk.predeparture.admin.dto.PreDepartureResourceTranslationFields;
import com.tiu.turk.predeparture.admin.dto.PreDepartureResourceTranslationsForm;
import com.tiu.turk.predeparture.common.entity.PreDepartureResourceEntity;
import com.tiu.turk.predeparture.common.entity.PreDepartureResourceTranslationEntity;
import com.tiu.turk.predeparture.common.repository.PreDepartureResourceRepository;
import com.tiu.turk.predeparture.web.dto.PreDepartureResourceView;
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
public class PreDepartureResourceService {
    private final PreDepartureResourceRepository repository;
    private final FileService fileService;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        PreDepartureResourceEntity r = this.getById(id);
        String sourceText = Stream.of(r.getTitle(), r.getDescription())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializePreDepartureResourceTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    @Transactional(readOnly = true)
    public List<PreDepartureResourceView> getEnabledResources(TranslationLocale locale) {
        return this.repository.findEnabledWithTranslations().stream()
                .map(r -> new PreDepartureResourceView(r, locale))
                .toList();
    }

    public List<PreDepartureResourceEntity> getAllResources() {
        return this.repository.findAllByOrderByCreatedAtDesc();
    }

    public PreDepartureResourceEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));
    }

    @Transactional
    public void update(Long id, String title, String description, MultipartFile file, Long authorId) throws IOException {
        PreDepartureResourceEntity resource = this.getById(id);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        resource.setTitle(title.trim());
        resource.setDescription(description);
        if (file != null && !file.isEmpty()) {
            FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "pre-departure", authorId);
            resource.setFile(stored);
        }
        this.repository.save(resource);
    }

    @Transactional
    public void create(String title, String description, MultipartFile file, Long authorId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A file is required.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "pre-departure", authorId);
        PreDepartureResourceEntity resource = new PreDepartureResourceEntity();
        resource.setTitle(title.trim());
        resource.setDescription(description);
        resource.setFile(stored);
        resource.setEnabled(true);
        resource.setAuthor(new UserEntity(authorId));
        this.repository.save(resource);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public PreDepartureResourceTranslationsForm emptyForm() {
        PreDepartureResourceTranslationsForm form = new PreDepartureResourceTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new PreDepartureResourceTranslationFields());
        }
        return form;
    }

    /** Creates a resource with all languages at once: EN → base entity, other locales → pre_departure_resource_i18n. */
    @Transactional
    public void createWithTranslations(PreDepartureResourceTranslationsForm form, MultipartFile file, Long authorId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("A file is required.");
        }
        PreDepartureResourceTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en == null || en.getTitle() == null || en.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required.");
        }
        FileEntity stored = this.fileService.store(file, file.getOriginalFilename(), "pre-departure", authorId);
        PreDepartureResourceEntity resource = new PreDepartureResourceEntity();
        resource.setTitle(en.getTitle().trim());
        resource.setDescription(trimToNull(en.getDescription()));
        resource.setFile(stored);
        resource.setEnabled(true);
        resource.setAuthor(new UserEntity(authorId));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            PreDepartureResourceTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            PreDepartureResourceTranslationEntity t = new PreDepartureResourceTranslationEntity();
            t.setPreDepartureResource(resource);
            t.setLocale(locale);
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
            resource.getTranslations().put(locale, t);
        }
        this.repository.save(resource);
    }

    private static boolean isAllBlank(PreDepartureResourceTranslationFields f) {
        return Stream.of(f.getTitle(), f.getDescription())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from pre_departure_resource_i18n. */
    @Transactional(readOnly = true)
    public PreDepartureResourceTranslationsForm getTranslationsForm(Long id) {
        PreDepartureResourceEntity r = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));
        PreDepartureResourceTranslationsForm form = new PreDepartureResourceTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            PreDepartureResourceTranslationFields f = new PreDepartureResourceTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setTitle(r.getTitle());
                f.setDescription(r.getDescription());
            } else {
                PreDepartureResourceTranslationEntity t = r.getTranslations().get(locale);
                if (t != null) {
                    f.setTitle(t.getTitle());
                    f.setDescription(t.getDescription());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the pre_departure_resource_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, PreDepartureResourceTranslationsForm form) {
        PreDepartureResourceEntity r = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            PreDepartureResourceTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            PreDepartureResourceTranslationEntity t = r.getTranslations().get(locale);
            if (t == null) {
                t = new PreDepartureResourceTranslationEntity();
                t.setPreDepartureResource(r);
                t.setLocale(locale);
                r.getTranslations().put(locale, t);
            }
            t.setTitle(trimToNull(f.getTitle()));
            t.setDescription(trimToNull(f.getDescription()));
        }
        this.repository.save(r);
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
