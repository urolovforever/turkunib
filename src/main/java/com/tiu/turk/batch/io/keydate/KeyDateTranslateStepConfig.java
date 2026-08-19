package com.tiu.turk.batch.io.keydate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.listener.FailureListenerFactory;
import com.tiu.turk.batch.io.common.reader.TaskReaderFactory;
import com.tiu.turk.batch.io.keydate.processor.KeyDateProcessorComposite;
import com.tiu.turk.batch.io.keydate.writer.KeyDateTranslationWriter;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.io.IOException;
import java.net.SocketTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Configuration
@RequiredArgsConstructor
public class KeyDateTranslateStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;
    private final BackOffPolicy exponentialBackoffPolicy;
    private final TaskReaderFactory taskReaderFactory;
    private final KeyDateTranslationWriter keyDateTranslationWriter;
    private final KeyDateProcessorComposite keyDateCompositeProcessor;
    private final FailureListenerFactory listenerFactory;

    @Bean
    public Step translateKeyDateStep() {
        return new StepBuilder("translateKeyDateStep", this.jobRepository)
                .<TranslationTaskEntity, TranslationResult>chunk(20, this.tx)
                .reader(this.taskReaderFactory.getDefaultRepositoryReader(AppModule.KEY_DATE))
                .processor((ItemProcessor<TranslationTaskEntity, TranslationResult>) this.keyDateCompositeProcessor)
                .writer(this.keyDateTranslationWriter)
                .faultTolerant()
                .skip(IllegalStateException.class)
                .skip(IllegalArgumentException.class)
                .skip(JsonProcessingException.class)
                .skip(NonTransientAiException.class)
                .skipLimit(10000)
                .retry(IOException.class)
                .retry(SocketTimeoutException.class)
                .retry(WebClientRequestException.class)
                .retry(WebClientResponseException.TooManyRequests.class)
                .retry(WebClientResponseException.ServiceUnavailable.class)
                .retry(WebClientResponseException.InternalServerError.class)
                .retry(WebClientResponseException.BadGateway.class)
                .retry(WebClientResponseException.GatewayTimeout.class)
                .retry(TransientDataAccessException.class)
                .retryLimit(5)
                .backOffPolicy(this.exponentialBackoffPolicy)
                .listener(this.listenerFactory.getTaskFailureListener())
                .listener(this.listenerFactory.getWriteFailureListener())
                .listener(this.listenerFactory.getReadFailureListener())
                .build();
    }
}
