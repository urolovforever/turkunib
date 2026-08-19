package com.tiu.turk.batch.io.researchproject.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.research.common.entity.ResearchProjectEntity;
import com.tiu.turk.research.common.repository.ResearchProjectRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResearchProjectTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final ResearchProjectRepository researchProjectRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        ResearchProjectEntity p = this.researchProjectRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Research project not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "title", p.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "subjectArea", p.getSubjectArea());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "description", p.getDescription());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "content", p.getContent());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "participatingUniversities", p.getParticipatingUniversities());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "title", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "subjectArea", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "description", MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "content", MetaType.HTML);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "participatingUniversities", MetaType.PLAIN_TEXT);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(p.getId(), item.getId(), item.getSourceHash(), AppModule.RESEARCH_PROJECT,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
