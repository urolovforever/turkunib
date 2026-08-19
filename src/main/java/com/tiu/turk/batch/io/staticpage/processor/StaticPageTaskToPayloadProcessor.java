package com.tiu.turk.batch.io.staticpage.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import com.tiu.turk.staticpage.common.repository.StaticPageRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StaticPageTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final StaticPageRepository staticPageRepository;

    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        StaticPageEntity staticPage = (StaticPageEntity)this.staticPageRepository.findById(item.getEntityId()).orElseThrow(() -> new IllegalStateException("Static Page not found: " + item.getEntityId()));
        StaticPageTranslationEntity enLocale = Optional.ofNullable((StaticPageTranslationEntity)staticPage.getTranslations().get(TranslationLocale.EN)).orElseThrow(() -> new IllegalStateException("Static Page 'en' translation is null: " + item.getEntityId()));
        LinkedHashMap fieldsDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"title", (String)enLocale.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"content", (String)enLocale.getContent());
        Map fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);
        LinkedHashMap metaDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"title", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"content", (MetaType)MetaType.HTML);
        Map metaData = Collections.unmodifiableMap(metaDataBuilder);
        return new SourcePayload(staticPage.getId(), item.getId(), item.getSourceHash(), AppModule.STATIC_PAGE, item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }

    @Generated
    public StaticPageTaskToPayloadProcessor(StaticPageRepository staticPageRepository) {
        this.staticPageRepository = staticPageRepository;
    }
}

