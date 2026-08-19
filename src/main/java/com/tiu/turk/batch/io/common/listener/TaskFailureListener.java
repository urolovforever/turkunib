package com.tiu.turk.batch.io.common.listener;

import com.tiu.turk.batch.io.common.AbstractFailureListener;
import com.tiu.turk.batch.io.common.service.TaskFailMarker;
import java.util.function.Function;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnSkipInProcess;

public class TaskFailureListener<T>
extends AbstractFailureListener<T> {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(TaskFailureListener.class);

    public TaskFailureListener(TaskFailMarker failMarker, Function<T, Long> taskIdExtractor) {
        super(failMarker, taskIdExtractor);
    }

    @OnProcessError
    public void onProcessError(T item, Exception e) {
        log.error("Spring-Batch: Processing error for item {}: {}", item, e.getMessage());
        this.mark(item, (Throwable)e);
    }

    @OnSkipInProcess
    public void onSkipInProcess(T item, Throwable t) {
        log.error("Spring-Batch: Skipped item {} due to error: {}", item, t.getMessage());
        this.mark(item, t);
    }
}

