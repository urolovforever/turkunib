package com.tiu.turk.photogallery.common.repository;

import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoGalleryTranslationRepository
extends JpaRepository<PhotoGalleryTranslationEntity, Long> {
}

