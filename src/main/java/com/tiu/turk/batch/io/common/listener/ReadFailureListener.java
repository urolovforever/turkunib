package com.tiu.turk.batch.io.common.listener;

import com.tiu.turk.batch.io.common.AbstractFailureListener;
import com.tiu.turk.batch.io.common.service.TaskFailMarker;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnSkipInRead;

public class ReadFailureListener<T>
extends AbstractFailureListener<T> {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ReadFailureListener.class);

    public ReadFailureListener(TaskFailMarker failMarker) {
        super(failMarker, item -> null);
    }

    @OnReadError
    public void onReadError(Exception e) {
        log.error("Spring-Batch: Read error: {}", e.getMessage());
    }

    @OnSkipInRead
    public void onSkipInRead(Throwable t) {
        log.error("Spring-Batch: Skipped due to error: {}", t.getMessage());
    }
}

