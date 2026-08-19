package com.tiu.turk.batch.io.news.writer;

import com.github.slugify.Slugify;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import com.tiu.turk.news.common.repository.NewsRepository;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class NewsTranslationWriter
implements ItemWriter<TranslationResult> {
    private final NewsRepository newsRepository;
    private final TranslationTaskRepository taskRepository;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.NEWS) continue;
            NewsEntity news = (NewsEntity)this.newsRepository.findById(item.entityId()).orElseThrow(() -> new IllegalArgumentException("News not found: " + item.entityId()));
            NewsTranslationEntity existedTranslation = Optional.ofNullable((NewsTranslationEntity)news.getTranslations().get(item.targetLocale())).orElse(new NewsTranslationEntity());
            if (existedTranslation.getId() == null) {
                existedTranslation.setNews(news);
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
                if (fields.containsKey("content")) {
                    existedTranslation.setContent((String)fields.get("content"));
                }
            }
            existedTranslation.setSlug(this.slugify.slugify(existedTranslation.getTitle()));
            existedTranslation.setUpdatedAt(LocalDateTime.now());
            news.getTranslations().put(item.targetLocale(), existedTranslation);
            this.newsRepository.save(news);
            this.taskRepository.markDone(AppModule.NEWS, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }

    @Generated
    public NewsTranslationWriter(NewsRepository newsRepository, TranslationTaskRepository taskRepository) {
        this.newsRepository = newsRepository;
        this.taskRepository = taskRepository;
    }
}

