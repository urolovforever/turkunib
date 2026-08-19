package com.tiu.turk.batch.io.photogallery.processor;

import com.tiu.turk.batch.io.common.dto.SourcePayload;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.processor.OpenAiTranslateProcessor;
import com.tiu.turk.batch.io.common.processor.ValidateSourcePayloadProcessor;
import com.tiu.turk.batch.io.photogallery.processor.PhotoGalleryTaskToPayloadProcessor;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import lombok.Generated;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PhotoGalleryCompositeProcessor
implements ItemProcessor<TranslationTaskEntity, TranslationResult> {
    private final PhotoGalleryTaskToPayloadProcessor taskToPayloadProcessor;
    private final OpenAiTranslateProcessor openAiTranslateProcessor;
    private final ValidateSourcePayloadProcessor validateSourcePayloadProcessor;

    public TranslationResult process(@NonNull TranslationTaskEntity task) throws Exception {
        SourcePayload payload = this.taskToPayloadProcessor.process(task);
        SourcePayload validatedPayload = this.validateSourcePayloadProcessor.process(payload);
        return this.openAiTranslateProcessor.process(validatedPayload);
    }

    @Generated
    public PhotoGalleryCompositeProcessor(PhotoGalleryTaskToPayloadProcessor taskToPayloadProcessor, OpenAiTranslateProcessor openAiTranslateProcessor, ValidateSourcePayloadProcessor validateSourcePayloadProcessor) {
        this.taskToPayloadProcessor = taskToPayloadProcessor;
        this.openAiTranslateProcessor = openAiTranslateProcessor;
        this.validateSourcePayloadProcessor = validateSourcePayloadProcessor;
    }
}

