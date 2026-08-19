package com.tiu.turk.batch.io.common.reader;

import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public class TaskReaderFactory {
    private final TranslationTaskRepository taskRepository;

    public RepositoryItemReader<TranslationTaskEntity> getDefaultRepositoryReader(AppModule module) {
        return new RepositoryItemReaderBuilder<TranslationTaskEntity>().name("translationTaskReader").repository(this.taskRepository).methodName("findPendingForModule").arguments(List.of(module, TranslationLocale.defaultTargetLocales())).pageSize(1).sorts(Map.of("id", Sort.Direction.ASC)).build();
    }

    @Generated
    public TaskReaderFactory(TranslationTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}

