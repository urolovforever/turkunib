package com.tiu.turk.translation.service;

import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.digest.Blake3;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TranslationInitializerService {
    private final JobLauncher jobLauncher;
    private final Job translationNewsJob;
    private final Job translationNewsCategoryJob;
    private final Job translationBannerJob;
    private final Job translatePhotoGalleryJob;
    private final Job translateEventJob;
    private final Job translateStaticPageJob;
    private final Job translateScholarshipJob;
    private final Job translateResearchProjectJob;
    private final Job translateWebinarJob;
    private final Job translateFaqJob;
    private final Job translatePublicationJob;
    private final Job translateLeadershipMemberJob;
    private final Job translateKeyDateJob;
    private final Job translateInstitutionalDocumentJob;
    private final Job translatePreDepartureResourceJob;
    private final TranslationTaskRepository taskRepository;

    public TranslationInitializerService(@Qualifier(value="asyncJobLauncher") JobLauncher jobLauncher, Job translationNewsJob, Job translationNewsCategoryJob, Job translationBannerJob, Job translatePhotoGalleryJob, Job translateEventJob, Job translateStaticPageJob, Job translateScholarshipJob, Job translateResearchProjectJob, Job translateWebinarJob, Job translateFaqJob, Job translatePublicationJob, Job translateLeadershipMemberJob, Job translateKeyDateJob, Job translateInstitutionalDocumentJob, Job translatePreDepartureResourceJob, TranslationTaskRepository taskRepository) {
        this.jobLauncher = jobLauncher;
        this.translationNewsCategoryJob = translationNewsCategoryJob;
        this.translationNewsJob = translationNewsJob;
        this.translationBannerJob = translationBannerJob;
        this.translatePhotoGalleryJob = translatePhotoGalleryJob;
        this.translateEventJob = translateEventJob;
        this.translateStaticPageJob = translateStaticPageJob;
        this.translateScholarshipJob = translateScholarshipJob;
        this.translateResearchProjectJob = translateResearchProjectJob;
        this.translateWebinarJob = translateWebinarJob;
        this.translateFaqJob = translateFaqJob;
        this.translatePublicationJob = translatePublicationJob;
        this.translateLeadershipMemberJob = translateLeadershipMemberJob;
        this.translateKeyDateJob = translateKeyDateJob;
        this.translateInstitutionalDocumentJob = translateInstitutionalDocumentJob;
        this.translatePreDepartureResourceJob = translatePreDepartureResourceJob;
        this.taskRepository = taskRepository;
    }

    public List<String> initializeNewsTranslationTasks(Long newsId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translationNewsJob, AppModule.NEWS, newsId, targetLocales, sourceText);
    }

    public List<String> initializeNewsCategoryTranslationTasks(Long categoryId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translationNewsCategoryJob, AppModule.NEWS_CATEGORY, categoryId, targetLocales, sourceText);
    }

    public List<String> initializeBannerTranslationTasks(Long bannerId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translationBannerJob, AppModule.BANNER, bannerId, targetLocales, sourceText);
    }

    public List<String> initializePhotoGalleryTranslationTasks(Long photoGalleryId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translatePhotoGalleryJob, AppModule.PHOTO_GALLERY, photoGalleryId, targetLocales, sourceText);
    }

    public List<String> initializeEventTranslationTasks(Long eventId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateEventJob, AppModule.EVENT, eventId, targetLocales, sourceText);
    }

    public List<String> initializeStaticPageTranslationTasks(Long staticPageId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateStaticPageJob, AppModule.STATIC_PAGE, staticPageId, targetLocales, sourceText);
    }

    public List<String> initializeScholarshipTranslationTasks(Long scholarshipId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateScholarshipJob, AppModule.SCHOLARSHIP, scholarshipId, targetLocales, sourceText);
    }

    public List<String> initializeResearchProjectTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateResearchProjectJob, AppModule.RESEARCH_PROJECT, entityId, targetLocales, sourceText);
    }

    public List<String> initializeWebinarTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateWebinarJob, AppModule.WEBINAR, entityId, targetLocales, sourceText);
    }

    public List<String> initializeFaqTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateFaqJob, AppModule.FAQ, entityId, targetLocales, sourceText);
    }

    public List<String> initializePublicationTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translatePublicationJob, AppModule.PUBLICATION, entityId, targetLocales, sourceText);
    }

    public List<String> initializeLeadershipMemberTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateLeadershipMemberJob, AppModule.LEADERSHIP_MEMBER, entityId, targetLocales, sourceText);
    }

    public List<String> initializeKeyDateTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateKeyDateJob, AppModule.KEY_DATE, entityId, targetLocales, sourceText);
    }

    public List<String> initializeInstitutionalDocumentTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translateInstitutionalDocumentJob, AppModule.INSTITUTIONAL_DOCUMENT, entityId, targetLocales, sourceText);
    }

    public List<String> initializePreDepartureResourceTranslationTasks(Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        return this.initializeTranslationTasks(this.translatePreDepartureResourceJob, AppModule.PRE_DEPARTURE_RESOURCE, entityId, targetLocales, sourceText);
    }

    private List<String> initializeTranslationTasks(Job job, AppModule module, Long entityId, List<TranslationLocale> targetLocales, String sourceText) throws Exception {
        if (targetLocales == null || targetLocales.isEmpty()) {
            throw new IllegalArgumentException("Target locales must be provided");
        }
        String sourceHash = this.generateSourceHash(sourceText);
        ArrayList<String> executedTranslations = new ArrayList<String>();
        for (TranslationLocale targetLocale : targetLocales) {
            if (this.taskRepository.isTaskExists(entityId, module, targetLocale, sourceHash)) continue;
            this.taskRepository.upsertPending(module.name(), entityId, sourceHash, TranslationLocale.EN.name(), targetLocale.name());
            executedTranslations.add(targetLocale.name());
        }
        JobParameters jobParameter = new JobParametersBuilder().addLong("ts", Long.valueOf(System.currentTimeMillis())).toJobParameters();
        this.jobLauncher.run(job, jobParameter);
        return executedTranslations;
    }

    private String generateSourceHash(String sourceText) {
        Blake3 hasher = Blake3.initHash();
        hasher.update(sourceText.getBytes(StandardCharsets.UTF_8));
        byte[] hash = new byte[32];
        hasher.doFinalize(hash);
        return this.toHex(hash);
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

