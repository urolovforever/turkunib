package com.tiu.turk.batch.io.leadershipmember.processor;

import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.processor.OpenAiTranslateProcessor;
import com.tiu.turk.batch.io.common.processor.ValidateSourcePayloadProcessor;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeadershipMemberProcessorComposite
implements ItemProcessor<TranslationTaskEntity, TranslationResult> {
    private final LeadershipMemberTaskToPayloadProcessor taskToPayloadProcessor;
    private final OpenAiTranslateProcessor openAiTranslateProcessor;
    private final ValidateSourcePayloadProcessor validateSourcePayloadProcessor;

    @Override
    public TranslationResult process(@NonNull TranslationTaskEntity task) throws Exception {
        SourcePayload payload = this.taskToPayloadProcessor.process(task);
        SourcePayload validatedPayload = this.validateSourcePayloadProcessor.process(payload);
        return this.openAiTranslateProcessor.process(validatedPayload);
    }
}
