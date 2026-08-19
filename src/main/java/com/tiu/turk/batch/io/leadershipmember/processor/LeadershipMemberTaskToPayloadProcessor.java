package com.tiu.turk.batch.io.leadershipmember.processor;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.batch.io.common.PayloadProcessorUtils;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.leadership.common.entity.LeadershipMemberEntity;
import com.tiu.turk.leadership.common.repository.LeadershipMemberRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeadershipMemberTaskToPayloadProcessor
implements ItemProcessor<TranslationTaskEntity, SourcePayload> {
    private final LeadershipMemberRepository leadershipMemberRepository;

    @Override
    public SourcePayload process(TranslationTaskEntity item) throws Exception {
        LeadershipMemberEntity m = this.leadershipMemberRepository.findById(item.getEntityId())
                .orElseThrow(() -> new IllegalStateException("Leadership member not found: " + item.getEntityId()));

        LinkedHashMap<String, String> fieldsDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "position", m.getPosition());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "organization", m.getOrganization());
        PayloadProcessorUtils.putIfNotBlank(fieldsDataBuilder, "bio", m.getBio());
        Map<String, String> fieldsData = Collections.unmodifiableMap(fieldsDataBuilder);

        LinkedHashMap<String, MetaType> metaDataBuilder = new LinkedHashMap<>();
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "position", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "organization", MetaType.PLAIN_TEXT);
        PayloadProcessorUtils.putIfNotNull(metaDataBuilder, "bio", MetaType.HTML);
        Map<String, MetaType> metaData = Collections.unmodifiableMap(metaDataBuilder);

        return new SourcePayload(m.getId(), item.getId(), item.getSourceHash(), AppModule.LEADERSHIP_MEMBER,
                item.getSourceLocale(), item.getTargetLocale(), fieldsData, metaData);
    }
}
