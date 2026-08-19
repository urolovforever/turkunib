package com.tiu.turk.news.admin.service.impl;

import com.github.slugify.Slugify;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.service.ImageService;
import com.tiu.turk.news.admin.service.NewsCategoryService;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.news.common.repository.NewsCategoryRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsCategoryServiceImpl
implements NewsCategoryService {
    private final NewsCategoryRepository newsCategoryRepository;
    private final ImageService imageService;
    private final TranslationInitializerService translationInitializerService;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public Page<NewsCategoryEntity> getAllCategories(int page, int size) {
        return this.newsCategoryRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public List<NewsCategoryEntity> getAllCategories() {
        return this.newsCategoryRepository.findAll();
    }

    public NewsCategoryEntity getCategoryById(Long id) {
        return (NewsCategoryEntity)this.newsCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }

    public NewsCategoryEntity saveCategory(MultipartFile file, NewsCategoryEntity category, Long authorId) throws IOException {
        NewsCategoryTranslationEntity categoryTranslations;
        if (file != null && !file.isEmpty()) {
            String fileName = Optional.ofNullable(file.getResource().getFilename()).orElse("image");
            category.setImage(this.imageService.store(file, fileName, "news_category", authorId));
        }
        EnumMap<TranslationLocale, NewsCategoryTranslationEntity> preparedTranslations = new EnumMap<TranslationLocale, NewsCategoryTranslationEntity>(TranslationLocale.class);
        if (category.getTranslations() != null && !category.getTranslations().isEmpty() && (categoryTranslations = (NewsCategoryTranslationEntity)category.getTranslations().get(TranslationLocale.EN)) != null) {
            NewsCategoryTranslationEntity translation = new NewsCategoryTranslationEntity();
            translation.setCategory(category);
            translation.setLocale(TranslationLocale.EN);
            translation.setTitle(categoryTranslations.getTitle());
            translation.setSlug(this.slugify.slugify(categoryTranslations.getTitle()));
            translation.setDescription(categoryTranslations.getDescription());
            preparedTranslations.put(TranslationLocale.EN, translation);
        }
        category.setTranslations(preparedTranslations);
        category.setAuthor(new UserEntity(authorId));
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return (NewsCategoryEntity)this.newsCategoryRepository.save(category);
    }

    public NewsCategoryEntity updateCategory(Long id, MultipartFile file, NewsCategoryEntity category, Long authorId) throws IOException {
        NewsCategoryEntity existingCategory = (NewsCategoryEntity)this.newsCategoryRepository.getReferenceById(id);
        if (file != null && !file.isEmpty()) {
            String fileName = Optional.ofNullable(file.getResource().getFilename()).orElse("image");
            Optional<ImageEntity> existingImageOpt = Optional.ofNullable(existingCategory.getImage());
            if (existingImageOpt.isPresent()) {
                this.imageService.deleteImage(existingImageOpt.get().getId());
            }
            existingCategory.setImage(this.imageService.store(file, fileName, "news_category", authorId));
        }
        if (category.getTranslations() != null && !category.getTranslations().isEmpty()) {
            for (Map.Entry entry : category.getTranslations().entrySet()) {
                TranslationLocale locale = (TranslationLocale)entry.getKey();
                NewsCategoryTranslationEntity categoryTranslation = (NewsCategoryTranslationEntity)entry.getValue();
                NewsCategoryTranslationEntity translation = (NewsCategoryTranslationEntity)existingCategory.getTranslations().get(locale);
                if (translation == null) {
                    translation = new NewsCategoryTranslationEntity();
                    translation.setCategory(existingCategory);
                    translation.setCreatedAt(LocalDateTime.now());
                }
                translation.setLocale(locale);
                translation.setTitle(categoryTranslation.getTitle());
                translation.setSlug(this.slugify.slugify(categoryTranslation.getTitle()));
                translation.setDescription(categoryTranslation.getDescription());
                translation.setUpdatedAt(LocalDateTime.now());
                existingCategory.getTranslations().put(locale, translation);
            }
        }
        existingCategory.setAuthor(new UserEntity(authorId));
        existingCategory.setUpdatedAt(LocalDateTime.now());
        return (NewsCategoryEntity)this.newsCategoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        NewsCategoryEntity category = (NewsCategoryEntity)this.newsCategoryRepository.getReferenceById(id);
        Optional.ofNullable(category.getImage()).ifPresent(image -> {
            try {
                this.imageService.deleteImage(category.getImage().getId());
            }
            catch (IOException iOException) {
                // empty catch block
            }
        });
        this.newsCategoryRepository.delete(category);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public List<String> executeTranslationTasks(Long categoryId) throws Exception {
        NewsCategoryEntity category = this.getCategoryById(categoryId);
        NewsCategoryTranslationEntity enLocale = (NewsCategoryTranslationEntity)category.getTranslations().get(TranslationLocale.EN);
        return this.translationInitializerService.initializeNewsCategoryTranslationTasks(categoryId, TranslationLocale.defaultTargetLocales(), enLocale.getTitle() + enLocale.getDescription());
    }

    @Generated
    public NewsCategoryServiceImpl(NewsCategoryRepository newsCategoryRepository, ImageService imageService, TranslationInitializerService translationInitializerService) {
        this.newsCategoryRepository = newsCategoryRepository;
        this.imageService = imageService;
        this.translationInitializerService = translationInitializerService;
    }
}

