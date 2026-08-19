package com.tiu.turk.batch.io.institutionaldocument.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentEntity;
import com.tiu.turk.institutionaldoc.common.repository.InstitutionalDocumentRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstitutionalDocumentTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final InstitutionalDocumentRepository institutionalDocumentRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        InstitutionalDocumentEntity d = this.institutionalDocumentRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Institutional document not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "title", d.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "description", d.getDescription());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "title", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "description", MetaType.HTML);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(d.getId(), item.getId(), item.getSourceHash(), AppModule.INSTITUTIONAL_DOCUMENT,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
