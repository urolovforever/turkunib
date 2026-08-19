package com.tiu.turk.batch.io.common;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DelayedTasklet
implements Tasklet {
    private final long delayInMillis;

    public DelayedTasklet(long delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Thread.sleep(this.delayInMillis);
        return RepeatStatus.FINISHED;
    }
}

