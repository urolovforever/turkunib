package com.tiu.turk.batch.io.keydate.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.keydate.common.entity.KeyDateEntity;
import com.tiu.turk.keydate.common.repository.KeyDateRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyDateTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final KeyDateRepository keyDateRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        KeyDateEntity k = this.keyDateRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Key date not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "title", k.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "description", k.getDescription());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "title", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "description", MetaType.HTML);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(k.getId(), item.getId(), item.getSourceHash(), AppModule.KEY_DATE,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
