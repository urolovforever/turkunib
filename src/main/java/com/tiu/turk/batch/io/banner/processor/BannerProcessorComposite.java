package com.tiu.turk.batch.io.banner.processor;

import com.tiu.turk.batch.io.banner.processor.BannerTaskToPayloadProcessor;
import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.processor.OpenAiTranslateProcessor;
import com.tiu.turk.batch.io.common.processor.ValidateSourcePayloadProcessor;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BannerProcessorComposite
implements ItemProcessor<TranslationTaskEntity, TranslationResult> {
    private final BannerTaskToPayloadProcessor taskToPayloadProcessor;
    private final OpenAiTranslateProcessor openAiBannerTranslateProcessor;
    private final ValidateSourcePayloadProcessor validateBannerSourcePayloadProcessor;

    public TranslationResult process(@NonNull TranslationTaskEntity item) throws Exception {
        SourcePayload payload = this.taskToPayloadProcessor.process(item);
        SourcePayload validatedPayload = this.validateBannerSourcePayloadProcessor.process(payload);
        return this.openAiBannerTranslateProcessor.process(validatedPayload);
    }

    @Generated
    public BannerProcessorComposite(BannerTaskToPayloadProcessor taskToPayloadProcessor, OpenAiTranslateProcessor openAiBannerTranslateProcessor, ValidateSourcePayloadProcessor validateBannerSourcePayloadProcessor) {
        this.taskToPayloadProcessor = taskToPayloadProcessor;
        this.openAiBannerTranslateProcessor = openAiBannerTranslateProcessor;
        this.validateBannerSourcePayloadProcessor = validateBannerSourcePayloadProcessor;
    }
}

