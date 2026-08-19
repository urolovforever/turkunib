package com.tiu.turk.batch.io.banner.writer;

import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import com.tiu.turk.banner.common.repository.BannerRepository;
import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class BannerTranslationWriter
implements ItemWriter<TranslationResult> {
    private final BannerRepository bannerRepository;
    private final TranslationTaskRepository taskRepository;

    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.BANNER) continue;
            BannerEntity banner = (BannerEntity)this.bannerRepository.findById(item.entityId()).orElseThrow(() -> new IllegalArgumentException("Banner not found: " + item.entityId()));
            BannerTranslationEntity existedTranslation = Optional.ofNullable((BannerTranslationEntity)banner.getTranslations().get(item.targetLocale())).orElse(new BannerTranslationEntity());
            if (existedTranslation.getId() == null) {
                existedTranslation.setBanner(banner);
                existedTranslation.setLocale(item.targetLocale());
                existedTranslation.setCreatedAt(LocalDateTime.now());
            }
            if (item.translatedFields() != null && !item.translatedFields().isEmpty()) {
                Map fields = item.translatedFields();
                if (fields.containsKey("title")) {
                    existedTranslation.setTitle((String)fields.get("title"));
                }
                if (fields.containsKey("shortTitle")) {
                    existedTranslation.setShortTitle((String)fields.get("shortTitle"));
                }
                if (fields.containsKey("description")) {
                    existedTranslation.setDescription((String)fields.get("description"));
                }
                if (fields.containsKey("urlTitle")) {
                    existedTranslation.setUrlTitle((String)fields.get("urlTitle"));
                }
            }
            existedTranslation.setUpdatedAt(LocalDateTime.now());
            banner.getTranslations().put(item.targetLocale(), existedTranslation);
            this.bannerRepository.save(banner);
            this.taskRepository.markDone(AppModule.BANNER, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }

    @Generated
    public BannerTranslationWriter(BannerRepository bannerRepository, TranslationTaskRepository taskRepository) {
        this.bannerRepository = bannerRepository;
        this.taskRepository = taskRepository;
    }
}

