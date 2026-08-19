package com.tiu.turk.batch.io.scholarship.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.scholarship.common.entity.ScholarshipTranslationEntity;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
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
public class ScholarshipTranslationWriter
implements ItemWriter<TranslationResult> {
    private final ScholarshipRepository scholarshipRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.SCHOLARSHIP) {
                continue;
            }
            ScholarshipEntity s = this.scholarshipRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Scholarship not found: " + item.entityId()));
            ScholarshipTranslationEntity translation = Optional.ofNullable(s.getTranslations().get(item.targetLocale()))
                    .orElseGet(ScholarshipTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setScholarship(s);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("title")) translation.setTitle(fields.get("title"));
                if (fields.containsKey("provider")) translation.setProvider(fields.get("provider"));
                if (fields.containsKey("description")) translation.setDescription(fields.get("description"));
                if (fields.containsKey("content")) translation.setContent(fields.get("content"));
                if (fields.containsKey("coverage")) translation.setCoverage(fields.get("coverage"));
                if (fields.containsKey("eligibility")) translation.setEligibility(fields.get("eligibility"));
                if (fields.containsKey("amount")) translation.setAmount(fields.get("amount"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            s.getTranslations().put(item.targetLocale(), translation);
            this.scholarshipRepository.save(s);
            this.taskRepository.markDone(AppModule.SCHOLARSHIP, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
