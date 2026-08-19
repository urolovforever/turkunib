package com.tiu.turk.banner.admin.service;

import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import com.tiu.turk.banner.common.repository.BannerRepository;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.service.ImageService;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDateTime;
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
public class BannerService {
    private final BannerRepository bannerRepository;
    private final ImageService imageService;
    private final TranslationInitializerService initializerService;

    public Page<BannerEntity> getAllBanners(int page, int size) {
        return this.bannerRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public BannerEntity getBannerById(Long id) {
        return (BannerEntity)this.bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Banner not found"));
    }

    public BannerEntity createBanner(MultipartFile file, BannerEntity banner, Long authorId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required for creating a banner");
        }
        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
        ImageEntity storedImage = this.imageService.store(file, fileName, "banner", authorId);
        banner.setImage(storedImage);
        BannerTranslationEntity enTranslation = (BannerTranslationEntity)banner.getTranslations().get(TranslationLocale.EN);
        if (enTranslation == null || enTranslation.getTitle() == null || enTranslation.getTitle().isBlank()) {
            throw new IllegalArgumentException("English title is required for creating a banner");
        }
        Map<TranslationLocale, BannerTranslationEntity> translations = banner.getTranslations();
        translations.entrySet().removeIf(entry -> {
            BannerTranslationEntity t = (BannerTranslationEntity)entry.getValue();
            return entry.getKey() != TranslationLocale.EN && (t == null || t.getTitle() == null || t.getTitle().isBlank());
        });
        for (Map.Entry entry : translations.entrySet()) {
            TranslationLocale locale = (TranslationLocale)entry.getKey();
            BannerTranslationEntity translation = (BannerTranslationEntity)entry.getValue();
            String safeTitle = Optional.ofNullable(translation.getTitle()).orElse("");
            String safeUrlTitle = Optional.ofNullable(translation.getUrlTitle()).orElse("");
            translation.setLocale(locale);
            translation.setTitle(safeTitle);
            translation.setUrlTitle(safeUrlTitle);
            translation.setShortTitle(translation.getShortTitle());
            translation.setDescription(translation.getDescription());
            translation.setUpdatedAt(LocalDateTime.now());
            translation.setCreatedAt(LocalDateTime.now());
            translation.setBanner(banner);
        }
        banner.setEnabled(banner.getEnabled());
        banner.setAuthor(new UserEntity(authorId));
        banner.setCreatedAt(LocalDateTime.now());
        banner.setUpdatedAt(LocalDateTime.now());
        return (BannerEntity)this.bannerRepository.save(banner);
    }

    public BannerEntity updateBanner(Long id, MultipartFile file, BannerEntity updatedBanner, Long authorId) throws IOException {
        BannerEntity existingBanner = this.getBannerById(id);
        Long imageIdToDelete = null;
        if (file != null && !file.isEmpty()) {
            if (existingBanner.getImage() != null) {
                imageIdToDelete = existingBanner.getImage().getId();
            }
            String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            ImageEntity storedImage = this.imageService.store(file, fileName, "banner", authorId);
            existingBanner.setImage(storedImage);
        }
        for (Map.Entry entry : updatedBanner.getTranslations().entrySet()) {
            TranslationLocale locale = (TranslationLocale)entry.getKey();
            BannerTranslationEntity updatedTranslation = (BannerTranslationEntity)entry.getValue();
            BannerTranslationEntity existingTranslation = (BannerTranslationEntity)existingBanner.getTranslations().get(locale);
            if (existingTranslation == null) {
                existingTranslation = new BannerTranslationEntity();
                existingTranslation.setBanner(existingBanner);
                existingTranslation.setCreatedAt(LocalDateTime.now());
            }
            String safeTitle = Optional.ofNullable(updatedTranslation.getTitle()).orElse("");
            String safeUrlTitle = Optional.ofNullable(updatedTranslation.getUrlTitle()).orElse("");
            existingTranslation.setLocale(locale);
            existingTranslation.setTitle(safeTitle);
            existingTranslation.setUrlTitle(safeUrlTitle);
            existingTranslation.setShortTitle(updatedTranslation.getShortTitle());
            existingTranslation.setDescription(updatedTranslation.getDescription());
            existingTranslation.setUpdatedAt(LocalDateTime.now());
            existingBanner.getTranslations().put(locale, existingTranslation);
        }
        existingBanner.setUrl(updatedBanner.getUrl());
        existingBanner.setPosition(updatedBanner.getPosition());
        existingBanner.setEnabled(updatedBanner.getEnabled());
        existingBanner.setAuthor(new UserEntity(authorId));
        existingBanner.setUpdatedAt(LocalDateTime.now());
        updatedBanner = (BannerEntity)this.bannerRepository.save(existingBanner);
        if (imageIdToDelete != null) {
            this.imageService.deleteImage(imageIdToDelete);
        }
        return updatedBanner;
    }

    public void deleteBanner(Long id) throws IOException {
        BannerEntity banner = this.getBannerById(id);
        Long imageIdToDelete = banner.getImage() != null ? banner.getImage().getId() : null;
        this.bannerRepository.delete(banner);
        if (imageIdToDelete != null) {
            this.imageService.deleteImage(imageIdToDelete);
        }
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public List<String> executeTranslationTasks(Long bannerId) throws Exception {
        BannerEntity banner = this.getBannerById(bannerId);
        BannerTranslationEntity enLocale = (BannerTranslationEntity)banner.getTranslations().get(TranslationLocale.EN);
        return this.initializerService.initializeBannerTranslationTasks(bannerId, TranslationLocale.defaultTargetLocales(), enLocale.getTitle() + enLocale.getShortTitle() + enLocale.getDescription());
    }

    @Generated
    public BannerService(BannerRepository bannerRepository, ImageService imageService, TranslationInitializerService initializerService) {
        this.bannerRepository = bannerRepository;
        this.imageService = imageService;
        this.initializerService = initializerService;
    }
}

