package com.tiu.turk.batch.io.common;

import com.tiu.turk.batch.io.common.service.TaskFailMarker;
import java.util.function.Function;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFailureListener<T> {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(AbstractFailureListener.class);
    protected final TaskFailMarker failMarker;
    protected final Function<T, Long> taskIdExtractor;

    protected void mark(T item, Throwable t) {
        try {
            Long taskId = (Long)this.taskIdExtractor.apply(item);
            String msg = t == null ? "Unknown error" : t.getClass().getSimpleName() + ": " + t.getMessage();
            this.failMarker.failTask(taskId, msg);
        }
        catch (Exception ex) {
            log.error("Spring-Batch: Failed to mark task as FAILED: {}", ex.getMessage());
        }
    }

    @Generated
    public AbstractFailureListener(TaskFailMarker failMarker, Function<T, Long> taskIdExtractor) {
        this.failMarker = failMarker;
        this.taskIdExtractor = taskIdExtractor;
    }
}

