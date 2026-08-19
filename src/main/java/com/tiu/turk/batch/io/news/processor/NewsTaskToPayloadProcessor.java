package com.tiu.turk.batch.io.news.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import com.tiu.turk.news.common.repository.NewsRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NewsTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final NewsRepository newsRepository;

    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        NewsEntity news = (NewsEntity)this.newsRepository.findById(item.getEntityId()).orElseThrow(() -> new IllegalStateException("News not found: " + item.getEntityId()));
        NewsTranslationEntity enLocale = Optional.ofNullable((NewsTranslationEntity)news.getTranslations().get(TranslationLocale.EN)).orElseThrow(() -> new IllegalStateException("News 'en' translation is null: " + item.getEntityId()));
        LinkedHashMap fieldsDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"title", (String)enLocale.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"description", (String)enLocale.getDescription());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"content", (String)enLocale.getContent());
        Map fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);
        LinkedHashMap metaDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"title", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"description", (MetaType)MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"content", (MetaType)MetaType.HTML);
        Map metaData = Collections.unmodifiableMap(metaDataBuilder);
        return new SourcePayload(news.getId(), item.getId(), item.getSourceHash(), AppModule.NEWS, item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }

    @Generated
    public NewsTaskToPayloadProcessor(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }
}

