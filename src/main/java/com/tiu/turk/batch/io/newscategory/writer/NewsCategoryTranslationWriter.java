package com.tiu.turk.batch.io.newscategory.writer;

import com.github.slugify.Slugify;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.news.common.repository.NewsCategoryRepository;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class NewsCategoryTranslationWriter
implements ItemWriter<TranslationResult> {
    private final NewsCategoryRepository newsCategoryRepository;
    private final TranslationTaskRepository taskRepository;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.NEWS_CATEGORY) continue;
            NewsCategoryEntity newsCategory = (NewsCategoryEntity)this.newsCategoryRepository.findById(item.entityId()).orElseThrow(() -> new IllegalArgumentException("News Category not found: " + item.entityId()));
            NewsCategoryTranslationEntity existedTranslation = Optional.ofNullable((NewsCategoryTranslationEntity)newsCategory.getTranslations().get(item.targetLocale())).orElse(new NewsCategoryTranslationEntity());
            if (existedTranslation.getId() == null) {
                existedTranslation.setCategory(newsCategory);
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
            newsCategory.getTranslations().put(item.targetLocale(), existedTranslation);
            this.newsCategoryRepository.save(newsCategory);
            this.taskRepository.markDone(AppModule.NEWS_CATEGORY, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }

    @Generated
    public NewsCategoryTranslationWriter(NewsCategoryRepository newsCategoryRepository, TranslationTaskRepository taskRepository) {
        this.newsCategoryRepository = newsCategoryRepository;
        this.taskRepository = taskRepository;
    }
}

