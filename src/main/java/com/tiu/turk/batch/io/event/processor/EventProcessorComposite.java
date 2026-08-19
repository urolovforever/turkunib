package com.tiu.turk.batch.io.event.processor;

import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.processor.OpenAiTranslateProcessor;
import com.tiu.turk.batch.io.common.processor.ValidateSourcePayloadProcessor;
import com.tiu.turk.batch.io.event.processor.EventTaskToPayloadProcessor;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EventProcessorComposite
implements ItemProcessor<TranslationTaskEntity, TranslationResult> {
    private final EventTaskToPayloadProcessor taskToPayloadProcessor;
    private final OpenAiTranslateProcessor openAiTranslateProcessor;
    private final ValidateSourcePayloadProcessor validateSourcePayloadProcessor;

    public TranslationResult process(TranslationTaskEntity task) throws Exception {
        SourcePayload payload = this.taskToPayloadProcessor.process(task);
        SourcePayload validatedPayload = this.validateSourcePayloadProcessor.process(payload);
        return this.openAiTranslateProcessor.process(validatedPayload);
    }

    @Generated
    public EventProcessorComposite(EventTaskToPayloadProcessor taskToPayloadProcessor, OpenAiTranslateProcessor openAiTranslateProcessor, ValidateSourcePayloadProcessor validateSourcePayloadProcessor) {
        this.taskToPayloadProcessor = taskToPayloadProcessor;
        this.openAiTranslateProcessor = openAiTranslateProcessor;
        this.validateSourcePayloadProcessor = validateSourcePayloadProcessor;
    }
}

