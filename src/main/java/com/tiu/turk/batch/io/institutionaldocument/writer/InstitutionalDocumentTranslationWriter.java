package com.tiu.turk.batch.io.institutionaldocument.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentEntity;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentTranslationEntity;
import com.tiu.turk.institutionaldoc.common.repository.InstitutionalDocumentRepository;
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
public class InstitutionalDocumentTranslationWriter
implements ItemWriter<TranslationResult> {
    private final InstitutionalDocumentRepository institutionalDocumentRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.INSTITUTIONAL_DOCUMENT) {
                continue;
            }
            InstitutionalDocumentEntity d = this.institutionalDocumentRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Institutional document not found: " + item.entityId()));
            InstitutionalDocumentTranslationEntity translation = Optional.ofNullable(d.getTranslations().get(item.targetLocale()))
                    .orElseGet(InstitutionalDocumentTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setInstitutionalDocument(d);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("title")) translation.setTitle(fields.get("title"));
                if (fields.containsKey("description")) translation.setDescription(fields.get("description"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            d.getTranslations().put(item.targetLocale(), translation);
            this.institutionalDocumentRepository.save(d);
            this.taskRepository.markDone(AppModule.INSTITUTIONAL_DOCUMENT, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
