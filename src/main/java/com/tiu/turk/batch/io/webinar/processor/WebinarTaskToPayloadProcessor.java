package com.tiu.turk.batch.io.webinar.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import com.tiu.turk.webinar.common.entity.WebinarEntity;
import com.tiu.turk.webinar.common.repository.WebinarRepository;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebinarTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final WebinarRepository webinarRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        WebinarEntity w = this.webinarRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Webinar not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "title", w.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "description", w.getDescription());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "content", w.getContent());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "title", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "description", MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "content", MetaType.HTML);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(w.getId(), item.getId(), item.getSourceHash(), AppModule.WEBINAR,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
