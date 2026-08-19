package com.tiu.turk.batch.io.common.listener;

import com.tiu.turk.batch.io.common.AbstractFailureListener;
import com.tiu.turk.batch.io.common.service.TaskFailMarker;
import java.util.function.Function;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.item.Chunk;

public class WriteFailureListener<T>
extends AbstractFailureListener<T> {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WriteFailureListener.class);

    public WriteFailureListener(TaskFailMarker failMarker, Function<T, Long> taskIdExtractor) {
        super(failMarker, taskIdExtractor);
    }

    @OnWriteError
    public void onWriteError(Exception e, Chunk<? extends T> items) {
        log.error("Spring-Batch: Write error for items {}: {}", items.getItems(), e.getMessage());
        for (T item : items) {
            this.mark(item, e);
        }
    }

    @OnSkipInWrite
    public void onSkipInWrite(T item, Throwable t) {
        log.error("Spring-Batch: Skipped item {} due to error: {}", item, t.getMessage());
        this.mark(item, t);
    }
}

