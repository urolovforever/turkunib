package com.tiu.turk.batch.io.photogallery.writer;

import com.github.slugify.Slugify;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import com.tiu.turk.photogallery.common.repository.PhotoGalleryRepository;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class PhotoGalleryTranslationWriter
implements ItemWriter<TranslationResult> {
    private final PhotoGalleryRepository photoGalleryRepository;
    private final TranslationTaskRepository taskRepository;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.PHOTO_GALLERY) continue;
            PhotoGalleryEntity photoGallery = (PhotoGalleryEntity)this.photoGalleryRepository.findById(item.entityId()).orElseThrow(() -> new IllegalArgumentException("PhotoGallery not found: " + item.entityId()));
            PhotoGalleryTranslationEntity existedTranslation = Optional.ofNullable((PhotoGalleryTranslationEntity)photoGallery.getTranslations().get(item.targetLocale())).orElseGet(PhotoGalleryTranslationEntity::new);
            if (existedTranslation.getId() == null) {
                existedTranslation.setPhotoGallery(photoGallery);
                existedTranslation.setLocale(item.targetLocale());
                existedTranslation.setCreatedAt(LocalDateTime.now());
            }
            if (item.translatedFields() != null && !item.translatedFields().isEmpty()) {
                Map fields = item.translatedFields();
                if (fields.containsKey("title")) {
                    existedTranslation.setTitle((String)fields.get("title"));
                }
                if (fields.containsKey("description")) {
                    existedTranslation.setDescription((String)fields.get("description"));
                }
            }
            existedTranslation.setUpdatedAt(LocalDateTime.now());
            existedTranslation.setSlug(this.slugify.slugify(existedTranslation.getTitle()));
            photoGallery.getTranslations().put(item.targetLocale(), existedTranslation);
            this.photoGalleryRepository.save(photoGallery);
            this.taskRepository.markDone(AppModule.PHOTO_GALLERY, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }

    @Generated
    public PhotoGalleryTranslationWriter(PhotoGalleryRepository photoGalleryRepository, TranslationTaskRepository taskRepository) {
        this.photoGalleryRepository = photoGalleryRepository;
        this.taskRepository = taskRepository;
    }
}

