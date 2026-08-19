package com.tiu.turk.photogallery.admin.service;

import com.github.slugify.Slugify;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.service.ImageService;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import com.tiu.turk.photogallery.common.repository.PhotoGalleryRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@Transactional
public class PhotoGalleryService {
    private final PhotoGalleryRepository photoGalleryRepository;
    private final ImageService imageService;
    private final TranslationInitializerService initializerService;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public Page<PhotoGalleryEntity> getAllGalleries(int page, int size) {
        return this.photoGalleryRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public PhotoGalleryEntity getGalleryById(Long galleryId) {
        return (PhotoGalleryEntity)this.photoGalleryRepository.findById(galleryId).orElseThrow(() -> new RuntimeException("Gallery not found with id: " + galleryId));
    }

    public PhotoGalleryEntity saveGallery(MultipartFile previewImage, MultipartFile[] imageFiles, PhotoGalleryEntity gallery, Long authorId) throws IOException {
        if (previewImage != null && !previewImage.isEmpty()) {
            String fileName = Optional.ofNullable(previewImage.getResource().getFilename()).orElse("image");
            gallery.setPreviewImage(this.imageService.store(previewImage, fileName, "photo_gallery", authorId));
        }
        if (imageFiles != null && imageFiles.length > 0) {
            ArrayList<ImageEntity> imageEntities = new ArrayList<ImageEntity>();
            for (MultipartFile file : imageFiles) {
                if (file == null || file.isEmpty()) continue;
                String fileName = Optional.ofNullable(file.getResource().getFilename()).orElse("image");
                ImageEntity imageEntity = this.imageService.store(file, fileName, "photo_gallery", authorId);
                imageEntities.add(imageEntity);
            }
            gallery.setImages(imageEntities);
        }
        Map<TranslationLocale, PhotoGalleryTranslationEntity> incomingTranslations = gallery.getTranslations();
        PhotoGalleryTranslationEntity enTranslation = incomingTranslations != null ? (PhotoGalleryTranslationEntity)incomingTranslations.get(TranslationLocale.EN) : null;
        if (enTranslation == null || Optional.ofNullable(enTranslation.getTitle()).orElse("").isBlank()) {
            throw new IllegalArgumentException("English (EN) title is required to create a photo gallery.");
        }
        EnumMap<TranslationLocale, PhotoGalleryTranslationEntity> resolvedTranslations = new EnumMap<TranslationLocale, PhotoGalleryTranslationEntity>(TranslationLocale.class);
        for (Map.Entry<TranslationLocale, PhotoGalleryTranslationEntity> entry : incomingTranslations.entrySet()) {
            TranslationLocale locale = (TranslationLocale)entry.getKey();
            PhotoGalleryTranslationEntity translation = (PhotoGalleryTranslationEntity)entry.getValue();
            if (translation == null) continue;
            String safeTitle = Optional.ofNullable(translation.getTitle()).orElse("");
            if (locale != TranslationLocale.EN && safeTitle.isBlank()) continue;
            String baseForSlug = safeTitle.isBlank() ? "gallery" : safeTitle;
            String safeDescription = Optional.ofNullable(translation.getDescription()).orElse("");
            translation.setLocale(locale);
            translation.setTitle(safeTitle);
            translation.setSlug(this.slugify.slugify(baseForSlug));
            translation.setDescription(safeDescription);
            translation.setPhotoGallery(gallery);
            translation.setCreatedAt(LocalDateTime.now());
            translation.setUpdatedAt(LocalDateTime.now());
            resolvedTranslations.put(locale, translation);
        }
        gallery.setTranslations(resolvedTranslations);
        gallery.setAuthor(new UserEntity(authorId));
        gallery.setCreatedAt(LocalDateTime.now());
        gallery.setUpdatedAt(LocalDateTime.now());
        return (PhotoGalleryEntity)this.photoGalleryRepository.save(gallery);
    }

    public PhotoGalleryEntity updateGallery(MultipartFile previewImage, MultipartFile[] imageFiles, PhotoGalleryEntity gallery, Long authorId) throws IOException {
        PhotoGalleryEntity existingGallery = (PhotoGalleryEntity)this.photoGalleryRepository.findById(gallery.getId()).orElseThrow(() -> new IllegalArgumentException("Gallery not found with id: " + gallery.getId()));
        if (previewImage != null && !previewImage.isEmpty()) {
            Optional<ImageEntity> existingImageOpt = Optional.ofNullable(existingGallery.getPreviewImage());
            if (existingImageOpt.isPresent()) {
                this.imageService.deleteImage(existingImageOpt.get().getId());
            }
            String string = Optional.ofNullable(previewImage.getResource().getFilename()).orElse("image");
            existingGallery.setPreviewImage(this.imageService.store(previewImage, string, "photo_gallery", authorId));
        }
        if (imageFiles != null && imageFiles.length > 0) {
            ArrayList<ImageEntity> imageEntities = new ArrayList<ImageEntity>();
            for (MultipartFile file : imageFiles) {
                if (file == null || file.isEmpty()) continue;
                String fileName = Optional.ofNullable(file.getResource().getFilename()).orElse("image");
                ImageEntity imageEntity = this.imageService.store(file, fileName, "photo_gallery", authorId);
                imageEntities.add(imageEntity);
            }
            if (!imageEntities.isEmpty()) {
                List<ImageEntity> list = existingGallery.getImages();
                if (list != null && !list.isEmpty()) {
                    for (ImageEntity image : list) {
                        this.imageService.deleteImage(image.getId());
                    }
                }
                existingGallery.setImages(imageEntities);
            }
        }
        for (Map.Entry entry : gallery.getTranslations().entrySet()) {
            String safeTitle;
            TranslationLocale locale = (TranslationLocale)entry.getKey();
            PhotoGalleryTranslationEntity updatedTranslation = (PhotoGalleryTranslationEntity)entry.getValue();
            PhotoGalleryTranslationEntity existingTranslation = (PhotoGalleryTranslationEntity)existingGallery.getTranslations().get(locale);
            if (existingTranslation == null) {
                existingTranslation = new PhotoGalleryTranslationEntity();
                existingTranslation.setCreatedAt(LocalDateTime.now());
                existingTranslation.setPhotoGallery(existingGallery);
            }
            String baseForSlug = (safeTitle = Optional.ofNullable(updatedTranslation.getTitle()).orElse("")).isBlank() ? "gallery" : safeTitle;
            String safeDescription = Optional.ofNullable(updatedTranslation.getDescription()).orElse("");
            existingTranslation.setLocale(locale);
            existingTranslation.setTitle(safeTitle);
            existingTranslation.setSlug(this.slugify.slugify(baseForSlug));
            existingTranslation.setDescription(safeDescription);
            existingTranslation.setUpdatedAt(LocalDateTime.now());
            existingGallery.getTranslations().put(locale, existingTranslation);
        }
        existingGallery.setEnabled(gallery.getEnabled());
        existingGallery.setAuthor(new UserEntity(authorId));
        existingGallery.setUpdatedAt(LocalDateTime.now());
        return (PhotoGalleryEntity)this.photoGalleryRepository.save(existingGallery);
    }

    public void deleteGallery(Long galleryId) throws IOException {
        List<ImageEntity> images;
        PhotoGalleryEntity gallery = (PhotoGalleryEntity)this.photoGalleryRepository.findById(galleryId).orElseThrow(() -> new IllegalArgumentException("Gallery not found with id: " + galleryId));
        Optional<ImageEntity> previewImgOpt = Optional.ofNullable(gallery.getPreviewImage());
        if (previewImgOpt.isPresent()) {
            this.imageService.deleteImage(previewImgOpt.get().getId());
        }
        if ((images = gallery.getImages()) != null && !images.isEmpty()) {
            for (ImageEntity image : images) {
                this.imageService.deleteImage(image.getId());
            }
        }
        this.photoGalleryRepository.delete(gallery);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public List<String> executeTranslationTask(Long galleryId) throws Exception {
        PhotoGalleryEntity gallery = this.getGalleryById(galleryId);
        PhotoGalleryTranslationEntity translation = (PhotoGalleryTranslationEntity)gallery.getTranslations().get(TranslationLocale.EN);
        return this.initializerService.initializePhotoGalleryTranslationTasks(galleryId, TranslationLocale.defaultTargetLocales(), translation.getTitle() + translation.getDescription());
    }

    @Generated
    public PhotoGalleryService(PhotoGalleryRepository photoGalleryRepository, ImageService imageService, TranslationInitializerService initializerService) {
        this.photoGalleryRepository = photoGalleryRepository;
        this.imageService = imageService;
        this.initializerService = initializerService;
    }
}

