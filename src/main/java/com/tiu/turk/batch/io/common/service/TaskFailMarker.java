package com.tiu.turk.batch.io.common.service;

import com.tiu.turk.common.enums.TranslationStatus;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskFailMarker {
    private final TranslationTaskRepository taskRepository;

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void failTask(Long taskId, String errorMessage) {
        this.taskRepository.findById(taskId).ifPresent(task -> {
            task.setStatus(TranslationStatus.FAILED);
            task.setAttempt(task.getAttempt() + 1);
            task.setErrorMessage(errorMessage != null && errorMessage.length() > 2000 ? errorMessage.substring(0, 2000) : errorMessage);
            this.taskRepository.save(task);
        });
    }

    @Generated
    public TaskFailMarker(TranslationTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}

