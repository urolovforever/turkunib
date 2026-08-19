package com.tiu.turk.batch.io.faq.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.faq.common.entity.FaqEntity;
import com.tiu.turk.faq.common.entity.FaqTranslationEntity;
import com.tiu.turk.faq.common.repository.FaqRepository;
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
public class FaqTranslationWriter
implements ItemWriter<TranslationResult> {
    private final FaqRepository faqRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.FAQ) {
                continue;
            }
            FaqEntity f = this.faqRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Faq not found: " + item.entityId()));
            FaqTranslationEntity translation = Optional.ofNullable(f.getTranslations().get(item.targetLocale()))
                    .orElseGet(FaqTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setFaq(f);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("question")) translation.setQuestion(fields.get("question"));
                if (fields.containsKey("answer")) translation.setAnswer(fields.get("answer"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            f.getTranslations().put(item.targetLocale(), translation);
            this.faqRepository.save(f);
            this.taskRepository.markDone(AppModule.FAQ, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
