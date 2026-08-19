package com.tiu.turk.batch.io.faq.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.faq.common.entity.FaqEntity;
import com.tiu.turk.faq.common.repository.FaqRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FaqTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final FaqRepository faqRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        FaqEntity f = this.faqRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Faq not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "question", f.getQuestion());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "answer", f.getAnswer());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "question", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "answer", MetaType.HTML);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(f.getId(), item.getId(), item.getSourceHash(), AppModule.FAQ,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
