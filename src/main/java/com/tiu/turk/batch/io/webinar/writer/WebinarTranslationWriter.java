package com.tiu.turk.batch.io.webinar.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import com.tiu.turk.webinar.common.entity.WebinarEntity;
import com.tiu.turk.webinar.common.entity.WebinarTranslationEntity;
import com.tiu.turk.webinar.common.repository.WebinarRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebinarTranslationWriter
implements ItemWriter<TranslationResult> {
    private final WebinarRepository webinarRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.WEBINAR) {
                continue;
            }
            WebinarEntity w = this.webinarRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Webinar not found: " + item.entityId()));
            WebinarTranslationEntity translation = Optional.ofNullable(w.getTranslations().get(item.targetLocale()))
                    .orElseGet(WebinarTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setWebinar(w);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("title")) translation.setTitle(fields.get("title"));
                if (fields.containsKey("description")) translation.setDescription(fields.get("description"));
                if (fields.containsKey("content")) translation.setContent(fields.get("content"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            w.getTranslations().put(item.targetLocale(), translation);
            this.webinarRepository.save(w);
            this.taskRepository.markDone(AppModule.WEBINAR, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
