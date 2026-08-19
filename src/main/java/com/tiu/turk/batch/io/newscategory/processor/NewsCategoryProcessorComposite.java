package com.tiu.turk.batch.io.newscategory.processor;

import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.processor.OpenAiTranslateProcessor;
import com.tiu.turk.batch.io.common.processor.ValidateSourcePayloadProcessor;
import com.tiu.turk.batch.io.newscategory.processor.NewsCategoryTaskToPayloadProcessor;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NewsCategoryProcessorComposite
implements ItemProcessor<TranslationTaskEntity, TranslationResult> {
    private final NewsCategoryTaskToPayloadProcessor taskToPayloadProcessor;
    private final OpenAiTranslateProcessor openAiTranslateProcessor;
    private final ValidateSourcePayloadProcessor validateSourcePayloadProcessor;

    public TranslationResult process(@NonNull TranslationTaskEntity item) throws Exception {
        SourcePayload payload = this.taskToPayloadProcessor.process(item);
        SourcePayload validatedPayload = this.validateSourcePayloadProcessor.process(payload);
        return this.openAiTranslateProcessor.process(validatedPayload);
    }

    @Generated
    public NewsCategoryProcessorComposite(NewsCategoryTaskToPayloadProcessor taskToPayloadProcessor, OpenAiTranslateProcessor openAiTranslateProcessor, ValidateSourcePayloadProcessor validateSourcePayloadProcessor) {
        this.taskToPayloadProcessor = taskToPayloadProcessor;
        this.openAiTranslateProcessor = openAiTranslateProcessor;
        this.validateSourcePayloadProcessor = validateSourcePayloadProcessor;
    }
}

