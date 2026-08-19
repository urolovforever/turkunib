package com.tiu.turk.batch.io.researchproject.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.research.common.entity.ResearchProjectEntity;
import com.tiu.turk.research.common.entity.ResearchProjectTranslationEntity;
import com.tiu.turk.research.common.repository.ResearchProjectRepository;
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
public class ResearchProjectTranslationWriter
implements ItemWriter<TranslationResult> {
    private final ResearchProjectRepository researchProjectRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.RESEARCH_PROJECT) {
                continue;
            }
            ResearchProjectEntity p = this.researchProjectRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Research project not found: " + item.entityId()));
            ResearchProjectTranslationEntity translation = Optional.ofNullable(p.getTranslations().get(item.targetLocale()))
                    .orElseGet(ResearchProjectTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setResearchProject(p);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("title")) translation.setTitle(fields.get("title"));
                if (fields.containsKey("subjectArea")) translation.setSubjectArea(fields.get("subjectArea"));
                if (fields.containsKey("description")) translation.setDescription(fields.get("description"));
                if (fields.containsKey("content")) translation.setContent(fields.get("content"));
                if (fields.containsKey("participatingUniversities")) translation.setParticipatingUniversities(fields.get("participatingUniversities"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            p.getTranslations().put(item.targetLocale(), translation);
            this.researchProjectRepository.save(p);
            this.taskRepository.markDone(AppModule.RESEARCH_PROJECT, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
