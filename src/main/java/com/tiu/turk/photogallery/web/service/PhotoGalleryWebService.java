package com.tiu.turk.photogallery.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.photogallery.common.dto.PhotoGallerySingleTranslationDto;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.common.repository.PhotoGalleryRepository;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PhotoGalleryWebService {
    private final PhotoGalleryRepository photoGalleryRepository;

    public List<PhotoGallerySingleTranslationDto> getActivePhotoGalleries(TranslationLocale locale, int limit) {
        return this.photoGalleryRepository.findActivePhotoGalleries(locale, (Pageable)PageRequest.ofSize((int)limit));
    }

    public Page<PhotoGallerySingleTranslationDto> getActivePhotoGalleriesPaginated(TranslationLocale locale, int page, int size) {
        return this.photoGalleryRepository.findActivePhotoGalleriesPage(locale, (Pageable)PageRequest.of((int)page, (int)size));
    }

    public Page<PhotoGallerySingleTranslationDto> searchActivePhotoGalleriesPaginated(TranslationLocale locale, String query, int page, int size) {
        return this.photoGalleryRepository.findActivePhotoGalleriesPageBySearch(locale, query, (Pageable)PageRequest.of((int)page, (int)size));
    }

    public PhotoGalleryEntity getPhotoGalleryById(TranslationLocale locale, Long photoGalleryId) {
        return (PhotoGalleryEntity)this.photoGalleryRepository.findByIdWithTranslations(photoGalleryId, locale).orElseThrow(() -> new RuntimeException("Photo Gallery not found"));
    }

    @Generated
    public PhotoGalleryWebService(PhotoGalleryRepository photoGalleryRepository) {
        this.photoGalleryRepository = photoGalleryRepository;
    }
}

