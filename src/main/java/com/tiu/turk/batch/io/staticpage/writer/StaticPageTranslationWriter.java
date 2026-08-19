package com.tiu.turk.batch.io.staticpage.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import com.tiu.turk.staticpage.common.repository.StaticPageRepository;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class StaticPageTranslationWriter
implements ItemWriter<TranslationResult> {
    private final StaticPageRepository staticPageRepository;
    private final TranslationTaskRepository taskRepository;

    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.STATIC_PAGE) continue;
            StaticPageEntity staticPage = (StaticPageEntity)this.staticPageRepository.findById(item.entityId()).orElseThrow(() -> new IllegalArgumentException("Static Page not found: " + item.entityId()));
            StaticPageTranslationEntity existedTranslation = Optional.ofNullable((StaticPageTranslationEntity)staticPage.getTranslations().get(item.targetLocale())).orElseGet(StaticPageTranslationEntity::new);
            if (existedTranslation.getId() == null) {
                existedTranslation.setStaticPage(staticPage);
                existedTranslation.setLocale(item.targetLocale());
                existedTranslation.setCreatedAt(LocalDateTime.now());
            }
            if (item.translatedFields() != null && !item.translatedFields().isEmpty()) {
                Map fields = item.translatedFields();
                if (fields.containsKey("title")) {
                    existedTranslation.setTitle((String)fields.get("title"));
                }
                if (fields.containsKey("content")) {
                    existedTranslation.setContent((String)fields.get("content"));
                }
            }
            existedTranslation.setUpdatedAt(LocalDateTime.now());
            staticPage.getTranslations().put(item.targetLocale(), existedTranslation);
            this.staticPageRepository.save(staticPage);
            this.taskRepository.markDone(AppModule.STATIC_PAGE, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }

    @Generated
    public StaticPageTranslationWriter(StaticPageRepository staticPageRepository, TranslationTaskRepository taskRepository) {
        this.staticPageRepository = staticPageRepository;
        this.taskRepository = taskRepository;
    }
}

