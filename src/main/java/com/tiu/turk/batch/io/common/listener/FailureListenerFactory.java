package com.tiu.turk.batch.io.common.listener;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.batch.io.common.listener.ReadFailureListener;
import com.tiu.turk.batch.io.common.listener.TaskFailureListener;
import com.tiu.turk.batch.io.common.listener.WriteFailureListener;
import com.tiu.turk.batch.io.common.service.TaskFailMarker;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import lombok.Generated;
import org.springframework.stereotype.Component;

@Component
public final class FailureListenerFactory {
    private final TaskFailMarker failMarker;

    public TaskFailureListener<TranslationTaskEntity> getTaskFailureListener() {
        return new TaskFailureListener<>(this.failMarker, TranslationTaskEntity::getId);
    }

    public WriteFailureListener<TranslationResult> getWriteFailureListener() {
        return new WriteFailureListener<>(this.failMarker, TranslationResult::taskId);
    }

    public ReadFailureListener<TranslationTaskEntity> getReadFailureListener() {
        return new ReadFailureListener<>(this.failMarker);
    }

    @Generated
    public FailureListenerFactory(TaskFailMarker failMarker) {
        this.failMarker = failMarker;
    }
}

