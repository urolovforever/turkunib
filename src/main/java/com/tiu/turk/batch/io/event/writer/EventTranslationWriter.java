package com.tiu.turk.batch.io.event.writer;

import com.github.slugify.Slugify;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.event.common.repository.EventRepository;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class EventTranslationWriter
implements ItemWriter<TranslationResult> {
    private final EventRepository eventRepository;
    private final TranslationTaskRepository taskRepository;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.EVENT) continue;
            EventEntity event = (EventEntity)this.eventRepository.findById(item.entityId()).orElseThrow(() -> new IllegalArgumentException("Event not found: " + item.entityId()));
            EventTranslationEntity existedTranslation = Optional.ofNullable((EventTranslationEntity)event.getTranslations().get(item.targetLocale())).orElseGet(EventTranslationEntity::new);
            if (existedTranslation.getId() == null) {
                existedTranslation.setEvent(event);
                existedTranslation.setLocale(item.targetLocale());
                existedTranslation.setCreatedAt(LocalDateTime.now());
            }
            if (item.translatedFields() != null && !item.translatedFields().isEmpty()) {
                Map fields = item.translatedFields();
                if (fields.containsKey("title")) {
                    existedTranslation.setTitle((String)fields.get("title"));
                }
                if (fields.containsKey("description")) {
                    existedTranslation.setDescription((String)fields.get("description"));
                }
            }
            existedTranslation.setSlug(this.slugify.slugify(existedTranslation.getTitle()));
            existedTranslation.setUpdatedAt(LocalDateTime.now());
            event.getTranslations().put(item.targetLocale(), existedTranslation);
            this.eventRepository.save(event);
            this.taskRepository.markDone(AppModule.EVENT, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }

    @Generated
    public EventTranslationWriter(EventRepository eventRepository, TranslationTaskRepository taskRepository) {
        this.eventRepository = eventRepository;
        this.taskRepository = taskRepository;
    }
}

