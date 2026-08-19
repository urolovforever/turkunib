package com.tiu.turk.batch.io.event.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.event.common.repository.EventRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EventTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final EventRepository eventRepository;

    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        EventEntity event = (EventEntity)this.eventRepository.findById(item.getEntityId()).orElseThrow(() -> new IllegalStateException("Event not found: " + item.getEntityId()));
        EventTranslationEntity enLocale = (EventTranslationEntity)event.getTranslations().get(item.getSourceLocale());
        LinkedHashMap fieldsDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"title", (String)enLocale.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, (String)"description", (String)enLocale.getDescription());
        Map fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);
        LinkedHashMap metaDataBuilder = new LinkedHashMap();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"title", (MetaType)MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, (String)"description", (MetaType)MetaType.PLAIN_TEXT);
        Map metaData = Collections.unmodifiableMap(metaDataBuilder);
        return new SourcePayload(event.getId(), item.getId(), item.getSourceHash(), AppModule.EVENT, item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }

    @Generated
    public EventTaskToPayloadProcessor(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}

