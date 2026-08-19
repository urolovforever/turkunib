package com.tiu.turk.batch.io.leadershipmember;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.listener.FailureListenerFactory;
import com.tiu.turk.batch.io.common.reader.TaskReaderFactory;
import com.tiu.turk.batch.io.leadershipmember.processor.LeadershipMemberProcessorComposite;
import com.tiu.turk.batch.io.leadershipmember.writer.LeadershipMemberTranslationWriter;
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
public class LeadershipMemberTranslateStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;
    private final BackOffPolicy exponentialBackoffPolicy;
    private final TaskReaderFactory taskReaderFactory;
    private final LeadershipMemberTranslationWriter leadershipMemberTranslationWriter;
    private final LeadershipMemberProcessorComposite leadershipMemberCompositeProcessor;
    private final FailureListenerFactory listenerFactory;

    @Bean
    public Step translateLeadershipMemberStep() {
        return new StepBuilder("translateLeadershipMemberStep", this.jobRepository)
                .<TranslationTaskEntity, TranslationResult>chunk(20, this.tx)
                .reader(this.taskReaderFactory.getDefaultRepositoryReader(AppModule.LEADERSHIP_MEMBER))
                .processor((ItemProcessor<TranslationTaskEntity, TranslationResult>) this.leadershipMemberCompositeProcessor)
                .writer(this.leadershipMemberTranslationWriter)
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
