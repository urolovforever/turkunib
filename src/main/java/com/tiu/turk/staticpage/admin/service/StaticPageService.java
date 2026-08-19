package com.tiu.turk.staticpage.admin.service;

import com.github.slugify.Slugify;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.common.StaticPageType;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import com.tiu.turk.staticpage.common.repository.StaticPageRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StaticPageService {
    private final StaticPageRepository staticPageRepository;
    private final TranslationInitializerService initializerService;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();
    private static final Set<String> FORBIDDEN_SLUGS = Set.of("admin", "login", "logout", "register", "user", "users", "api", "static", "resources", "news", "events", "members", "photos", "uploads", "en", "kz", "kg", "uz", "tr");

    public Page<StaticPageEntity> getAllPages(int page, int size) {
        return this.staticPageRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public StaticPageEntity getStaticPageById(Long staticPageId) {
        return (StaticPageEntity)this.staticPageRepository.findById(staticPageId).orElseThrow(() -> new RuntimeException("Static page not found with id: " + staticPageId));
    }

    public StaticPageEntity saveStaticPage(StaticPageEntity newStaticPage, Long authorId) throws Exception {
        this.validateSlug(newStaticPage.getSlug());
        Map<TranslationLocale, StaticPageTranslationEntity> submitted = newStaticPage.getTranslations();
        if (submitted == null) {
            submitted = new EnumMap<TranslationLocale, StaticPageTranslationEntity>(TranslationLocale.class);
        }
        StaticPageTranslationEntity enTranslation = (StaticPageTranslationEntity)submitted.get(TranslationLocale.EN);
        if (enTranslation == null || enTranslation.getTitle() == null || enTranslation.getTitle().isBlank()) {
            throw new IllegalArgumentException("The English title is required.");
        }
        Map<TranslationLocale, StaticPageTranslationEntity> translations = new EnumMap<TranslationLocale, StaticPageTranslationEntity>(TranslationLocale.class);
        submitted.forEach((locale, translation) -> {
            if (locale == null || translation == null) {
                return;
            }
            if (translation.getTitle() == null || translation.getTitle().isBlank()) {
                return;
            }
            translation.setLocale(locale);
            translation.setStaticPage(newStaticPage);
            translation.setCreatedAt(LocalDateTime.now());
            translation.setUpdatedAt(LocalDateTime.now());
            translations.put(locale, translation);
        });
        newStaticPage.setTranslations(translations);
        newStaticPage.setSlug(this.slugify.slugify(newStaticPage.getSlug()));
        newStaticPage.setAuthor(new UserEntity(authorId));
        newStaticPage.setType(StaticPageType.CUSTOM);
        newStaticPage.setCreatedAt(LocalDateTime.now());
        newStaticPage.setUpdatedAt(LocalDateTime.now());
        return (StaticPageEntity)this.staticPageRepository.save(newStaticPage);
    }

    public StaticPageEntity updateStaticPage(StaticPageEntity updatedStaticPage, Long authorId) throws Exception {
        this.validateSlug(updatedStaticPage.getSlug());
        StaticPageEntity existingPage = this.getStaticPageById(updatedStaticPage.getId());
        updatedStaticPage.getTranslations().forEach((locale, translation) -> {
            StaticPageTranslationEntity existingTranslation = (StaticPageTranslationEntity)existingPage.getTranslations().get(locale);
            if (existingTranslation == null || existingTranslation.getId() == null) {
                existingTranslation = new StaticPageTranslationEntity();
                existingTranslation.setStaticPage(existingPage);
                existingTranslation.setLocale(locale);
                existingTranslation.setCreatedAt(LocalDateTime.now());
            }
            existingTranslation.setTitle(translation.getTitle());
            existingTranslation.setContent(translation.getContent());
            existingTranslation.setUpdatedAt(LocalDateTime.now());
            existingPage.getTranslations().put(locale, existingTranslation);
        });
        existingPage.setSlug(this.slugify.slugify(updatedStaticPage.getSlug()));
        existingPage.setEnabled(updatedStaticPage.getEnabled());
        existingPage.setAuthor(new UserEntity(authorId));
        existingPage.setUpdatedAt(LocalDateTime.now());
        return (StaticPageEntity)this.staticPageRepository.save(existingPage);
    }

    public void deleteStaticPage(Long staticPageId) {
        StaticPageEntity existingPage = this.getStaticPageById(staticPageId);
        this.staticPageRepository.delete(existingPage);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public List<String> executeTranslationTasks(Long staticPageId) throws Exception {
        StaticPageEntity staticPage = this.getStaticPageById(staticPageId);
        StaticPageTranslationEntity enLocale = (StaticPageTranslationEntity)staticPage.getTranslations().get(TranslationLocale.EN);
        return this.initializerService.initializeStaticPageTranslationTasks(staticPageId, TranslationLocale.defaultTargetLocales(), enLocale.getTitle() + enLocale.getContent());
    }

    private void validateSlug(String slug) throws Exception {
        if (FORBIDDEN_SLUGS.contains(slug)) {
            throw new IllegalArgumentException("The slug '" + slug + "' is forbidden. Please choose a different slug.");
        }
    }

    @Generated
    public StaticPageService(StaticPageRepository staticPageRepository, TranslationInitializerService initializerService) {
        this.staticPageRepository = staticPageRepository;
        this.initializerService = initializerService;
    }
}

