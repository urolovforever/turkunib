package com.tiu.turk.batch.io.predeparture.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.predeparture.common.entity.PreDepartureResourceEntity;
import com.tiu.turk.predeparture.common.entity.PreDepartureResourceTranslationEntity;
import com.tiu.turk.predeparture.common.repository.PreDepartureResourceRepository;
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
public class PreDepartureResourceTranslationWriter
implements ItemWriter<TranslationResult> {
    private final PreDepartureResourceRepository preDepartureResourceRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.PRE_DEPARTURE_RESOURCE) {
                continue;
            }
            PreDepartureResourceEntity r = this.preDepartureResourceRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Pre-departure resource not found: " + item.entityId()));
            PreDepartureResourceTranslationEntity translation = Optional.ofNullable(r.getTranslations().get(item.targetLocale()))
                    .orElseGet(PreDepartureResourceTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setPreDepartureResource(r);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("title")) translation.setTitle(fields.get("title"));
                if (fields.containsKey("description")) translation.setDescription(fields.get("description"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            r.getTranslations().put(item.targetLocale(), translation);
            this.preDepartureResourceRepository.save(r);
            this.taskRepository.markDone(AppModule.PRE_DEPARTURE_RESOURCE, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
