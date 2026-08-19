package com.tiu.turk.batch.io.common;

import com.tiu.turk.batch.io.common.DelayedTasklet;
import lombok.Generated;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DelayedTaskletConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;

    @Bean
    public Step delayStep() {
        return new StepBuilder("delayStep", this.jobRepository).tasklet((Tasklet)new DelayedTasklet(6000L), this.tx).build();
    }

    @Generated
    public DelayedTaskletConfig(JobRepository jobRepository, PlatformTransactionManager tx) {
        this.jobRepository = jobRepository;
        this.tx = tx;
    }
}

