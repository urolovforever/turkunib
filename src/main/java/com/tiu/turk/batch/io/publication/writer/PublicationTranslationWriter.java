package com.tiu.turk.batch.io.publication.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.publication.common.entity.PublicationEntity;
import com.tiu.turk.publication.common.entity.PublicationTranslationEntity;
import com.tiu.turk.publication.common.repository.PublicationRepository;
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
public class PublicationTranslationWriter
implements ItemWriter<TranslationResult> {
    private final PublicationRepository publicationRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.PUBLICATION) {
                continue;
            }
            PublicationEntity p = this.publicationRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Publication not found: " + item.entityId()));
            PublicationTranslationEntity translation = Optional.ofNullable(p.getTranslations().get(item.targetLocale()))
                    .orElseGet(PublicationTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setPublication(p);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("title")) translation.setTitle(fields.get("title"));
                if (fields.containsKey("description")) translation.setDescription(fields.get("description"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            p.getTranslations().put(item.targetLocale(), translation);
            this.publicationRepository.save(p);
            this.taskRepository.markDone(AppModule.PUBLICATION, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
