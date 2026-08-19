package com.tiu.turk.photogallery.common.repository;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.photogallery.common.dto.PhotoGallerySingleTranslationDto;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoGalleryRepository
extends JpaRepository<PhotoGalleryEntity, Long> {
    @Query(value="select new com.tiu.turk.photogallery.common.dto.PhotoGallerySingleTranslationDto(\n    p.id, t.locale, t.title, t.slug, t.description, p.previewImage, null, p.createdAt\n)\nfrom PhotoGalleryEntity p\njoin p.translations t\nwhere p.enabled = true\nand t.locale = (case when exists (select 1 from PhotoGalleryTranslationEntity px where px.photoGallery = p and px.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\norder by p.createdAt desc\n")
    public List<PhotoGallerySingleTranslationDto> findActivePhotoGalleries(@Param("locale") TranslationLocale locale, Pageable pageable);

    @Query(value="select new com.tiu.turk.photogallery.common.dto.PhotoGallerySingleTranslationDto(\n    p.id, t.locale, t.title, t.slug, t.description, p.previewImage, null, p.createdAt\n)\nfrom PhotoGalleryEntity p\njoin p.translations t\nwhere p.enabled = true\nand t.locale = (case when exists (select 1 from PhotoGalleryTranslationEntity px where px.photoGallery = p and px.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\norder by p.createdAt desc\n")
    public Page<PhotoGallerySingleTranslationDto> findActivePhotoGalleriesPage(@Param("locale") TranslationLocale locale, Pageable pageable);

    @Query(value="select new com.tiu.turk.photogallery.common.dto.PhotoGallerySingleTranslationDto(\n    p.id, t.locale, t.title, t.slug, t.description, p.previewImage, null, p.createdAt\n)\nfrom PhotoGalleryEntity p\njoin p.translations t\nwhere p.enabled = true\nand t.locale = (case when exists (select 1 from PhotoGalleryTranslationEntity px where px.photoGallery = p and px.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\nand (lower(t.title) like lower(concat('%', :query, '%')) or lower(t.description) like lower(concat('%', :query, '%')))\norder by p.createdAt desc\n")
    public Page<PhotoGallerySingleTranslationDto> findActivePhotoGalleriesPageBySearch(@Param("locale") TranslationLocale locale, @Param("query") String query, Pageable pageable);

    @Query(value="select p from PhotoGalleryEntity p\njoin p.translations t\nwhere p.enabled = true\nand p.id = :id\nand t.locale = (case when exists (select 1 from PhotoGalleryTranslationEntity px where px.photoGallery = p and px.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\n")
    public Optional<PhotoGalleryEntity> findByIdWithTranslations(@Param("id") Long id, @Param("locale") TranslationLocale locale);
}

