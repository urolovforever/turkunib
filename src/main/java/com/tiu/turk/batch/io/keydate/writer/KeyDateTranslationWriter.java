package com.tiu.turk.batch.io.keydate.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.keydate.common.entity.KeyDateEntity;
import com.tiu.turk.keydate.common.entity.KeyDateTranslationEntity;
import com.tiu.turk.keydate.common.repository.KeyDateRepository;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyDateTranslationWriter
implements ItemWriter<TranslationResult> {
    private final KeyDateRepository keyDateRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.KEY_DATE) {
                continue;
            }
            KeyDateEntity k = this.keyDateRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Key date not found: " + item.entityId()));
            KeyDateTranslationEntity translation = Optional.ofNullable(k.getTranslations().get(item.targetLocale()))
                    .orElseGet(KeyDateTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setKeyDate(k);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("title")) translation.setTitle(fields.get("title"));
                if (fields.containsKey("description")) translation.setDescription(fields.get("description"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            k.getTranslations().put(item.targetLocale(), translation);
            this.keyDateRepository.save(k);
            this.taskRepository.markDone(AppModule.KEY_DATE, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
