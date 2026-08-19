package com.tiu.turk.batch.io.scholarship.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScholarshipTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final ScholarshipRepository scholarshipRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        ScholarshipEntity s = this.scholarshipRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Scholarship not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "title", s.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "provider", s.getProvider());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "description", s.getDescription());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "content", s.getContent());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "coverage", s.getCoverage());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "eligibility", s.getEligibility());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "amount", s.getAmount());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "title", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "provider", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "description", MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "content", MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "coverage", MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "eligibility", MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "amount", MetaType.PLAIN_TEXT);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(s.getId(), item.getId(), item.getSourceHash(), AppModule.SCHOLARSHIP,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
