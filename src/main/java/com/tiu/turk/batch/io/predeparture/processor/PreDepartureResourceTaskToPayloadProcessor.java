package com.tiu.turk.batch.io.predeparture.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.predeparture.common.entity.PreDepartureResourceEntity;
import com.tiu.turk.predeparture.common.repository.PreDepartureResourceRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PreDepartureResourceTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final PreDepartureResourceRepository preDepartureResourceRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        PreDepartureResourceEntity r = this.preDepartureResourceRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Pre-departure resource not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "title", r.getTitle());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "description", r.getDescription());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "title", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "description", MetaType.HTML);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(r.getId(), item.getId(), item.getSourceHash(), AppModule.PRE_DEPARTURE_RESOURCE,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
