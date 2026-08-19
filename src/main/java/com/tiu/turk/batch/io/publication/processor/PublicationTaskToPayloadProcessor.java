package com.tiu.turk.batch.io.publication.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.publication.common.entity.PublicationEntity;
import com.tiu.turk.publication.common.repository.PublicationRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublicationTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final PublicationRepository publicationRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        PublicationEntity p = this.publicationRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Publication not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "title", p.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "description", p.getDescription());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "title", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "description", MetaType.HTML);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(p.getId(), item.getId(), item.getSourceHash(), AppModule.PUBLICATION,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
