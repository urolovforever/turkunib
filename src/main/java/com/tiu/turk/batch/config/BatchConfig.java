package com.tiu.turk.batch.config;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

@Configuration
public class BatchConfig {
    public TaskExecutor batchTaskExecutor() {
        return new VirtualThreadTaskExecutor("v-thread-batch-");
    }

    @Bean
    public BackOffPolicy exponentialBackOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(2000L);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(30000L);
        return backOffPolicy;
    }

    @Bean(name={"asyncJobLauncher"})
    public JobLauncher asyncJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(this.batchTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}

