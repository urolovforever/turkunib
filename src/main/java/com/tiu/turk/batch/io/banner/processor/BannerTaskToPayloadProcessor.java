package com.tiu.turk.batch.io.banner.processor;

import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import com.tiu.turk.banner.common.repository.BannerRepository;
import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class BannerTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final BannerRepository bannerRepository;

    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        BannerEntity banner = (BannerEntity)this.bannerRepository.findById(item.getEntityId()).orElseThrow(() -> new IllegalStateException("Banner not found: " + item.getEntityId()));
        BannerTranslationEntity enLocale = Optional.ofNullable((BannerTranslationEntity)banner.getTranslations().get(TranslationLocale.EN)).orElseThrow(() -> new IllegalStateException("Banner 'en' translation is null: " + item.getEntityId()));
        LinkedHashMap fieldsDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"title", (String)enLocale.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"shortTitle", (String)enLocale.getShortTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"description", (String)enLocale.getDescription());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"urlTitle", (String)enLocale.getUrlTitle());
        Map fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);
        LinkedHashMap metaDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"title", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"shortTitle", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"description", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"urlTitle", (MetaType)MetaType.PLAIN_TEXT);
        Map metaData = Collections.unmodifiableMap(metaDataBuilder);
        return new SourcePayload(banner.getId(), item.getId(), item.getSourceHash(), AppModule.BANNER, item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }

    @Generated
    public BannerTaskToPayloadProcessor(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }
}

