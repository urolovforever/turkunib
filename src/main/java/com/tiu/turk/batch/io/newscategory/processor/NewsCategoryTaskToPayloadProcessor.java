package com.tiu.turk.batch.io.newscategory.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.news.common.repository.NewsCategoryRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NewsCategoryTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final NewsCategoryRepository newsCategoryRepository;

    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        NewsCategoryEntity newsCategory = (NewsCategoryEntity)this.newsCategoryRepository.findById(item.getEntityId()).orElseThrow(() -> new IllegalStateException("News Category not found: " + item.getEntityId()));
        NewsCategoryTranslationEntity enLocale = Optional.ofNullable((NewsCategoryTranslationEntity)newsCategory.getTranslations().get(item.getSourceLocale())).orElseThrow(() -> new IllegalStateException("News Category 'en' translation is null: " + item.getEntityId()));
        LinkedHashMap fieldsDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"title", (String)enLocale.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"description", (String)enLocale.getDescription());
        Map fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);
        LinkedHashMap metaDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"title", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"description", (MetaType)MetaType.PLAIN_TEXT);
        Map metaData = Collections.unmodifiableMap(metaDataBuilder);
        return new SourcePayload(newsCategory.getId(), item.getId(), item.getSourceHash(), AppModule.NEWS_CATEGORY, item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }

    @Generated
    public NewsCategoryTaskToPayloadProcessor(NewsCategoryRepository newsCategoryRepository) {
        this.newsCategoryRepository = newsCategoryRepository;
    }
}

