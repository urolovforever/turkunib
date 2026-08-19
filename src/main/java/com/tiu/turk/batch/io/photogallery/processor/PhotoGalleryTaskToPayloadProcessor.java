package com.tiu.turk.batch.io.photogallery.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import com.tiu.turk.photogallery.common.repository.PhotoGalleryRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PhotoGalleryTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final PhotoGalleryRepository photoGalleryRepository;

    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        PhotoGalleryEntity photoGallery = (PhotoGalleryEntity)this.photoGalleryRepository.findById(item.getEntityId()).orElseThrow(() -> new IllegalStateException("Photo Gallery not found: " + item.getEntityId()));
        PhotoGalleryTranslationEntity enLocale = Optional.ofNullable((PhotoGalleryTranslationEntity)photoGallery.getTranslations().get(TranslationLocale.EN)).orElseThrow(() -> new IllegalStateException("Photo Gallery 'en' translation is null: " + item.getEntityId()));
        LinkedHashMap fieldsDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"title", (String)enLocale.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"description", (String)enLocale.getDescription());
        Map fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);
        LinkedHashMap metaDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"title", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"description", (MetaType)MetaType.PLAIN_TEXT);
        Map metaData = Collections.unmodifiableMap(metaDataBuilder);
        return new SourcePayload(photoGallery.getId(), item.getId(), item.getSourceHash(), AppModule.PHOTO_GALLERY, item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }

    @Generated
    public PhotoGalleryTaskToPayloadProcessor(PhotoGalleryRepository photoGalleryRepository) {
        this.photoGalleryRepository = photoGalleryRepository;
    }
}

