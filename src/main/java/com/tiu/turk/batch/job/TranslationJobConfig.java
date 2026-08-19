package com.tiu.turk.batch.job;

import lombok.Generated;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslationJobConfig {
    private final JobRepository jobRepository;
    private final Step translateNewsStep;
    private final Step translateNewsCategoryStep;
    private final Step translateBannerStep;
    private final Step translatePhotoGalleryStep;
    private final Step translateEventStep;
    private final Step translateStaticPageStep;
    private final Step translateScholarshipStep;
    private final Step translateResearchProjectStep;
    private final Step translateWebinarStep;
    private final Step translateFaqStep;
    private final Step translatePublicationStep;
    private final Step translateLeadershipMemberStep;
    private final Step translateKeyDateStep;
    private final Step translateInstitutionalDocumentStep;
    private final Step translatePreDepartureResourceStep;
    private final Step delayStep;

    @Bean
    public Job translationNewsJob() {
        return new JobBuilder("translationNewsJob", this.jobRepository).start(this.translateNewsStep).next(this.delayStep).build();
    }

    @Bean
    public Job translationNewsCategoryJob() {
        return new JobBuilder("translationNewsCategoryJob", this.jobRepository).start(this.translateNewsCategoryStep).next(this.delayStep).build();
    }

    @Bean
    public Job translationBannerJob() {
        return new JobBuilder("translationBannerJob", this.jobRepository).start(this.translateBannerStep).next(this.delayStep).build();
    }

    @Bean
    public Job translatePhotoGalleryJob() {
        return new JobBuilder("translatePhotoGalleryJob", this.jobRepository).start(this.translatePhotoGalleryStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateEventJob() {
        return new JobBuilder("translateEventJob", this.jobRepository).start(this.translateEventStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateStaticPageJob() {
        return new JobBuilder("translateStaticPageJob", this.jobRepository).start(this.translateStaticPageStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateScholarshipJob() {
        return new JobBuilder("translateScholarshipJob", this.jobRepository).start(this.translateScholarshipStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateResearchProjectJob() {
        return new JobBuilder("translateResearchProjectJob", this.jobRepository).start(this.translateResearchProjectStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateWebinarJob() {
        return new JobBuilder("translateWebinarJob", this.jobRepository).start(this.translateWebinarStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateFaqJob() {
        return new JobBuilder("translateFaqJob", this.jobRepository).start(this.translateFaqStep).next(this.delayStep).build();
    }

    @Bean
    public Job translatePublicationJob() {
        return new JobBuilder("translatePublicationJob", this.jobRepository).start(this.translatePublicationStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateLeadershipMemberJob() {
        return new JobBuilder("translateLeadershipMemberJob", this.jobRepository).start(this.translateLeadershipMemberStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateKeyDateJob() {
        return new JobBuilder("translateKeyDateJob", this.jobRepository).start(this.translateKeyDateStep).next(this.delayStep).build();
    }

    @Bean
    public Job translateInstitutionalDocumentJob() {
        return new JobBuilder("translateInstitutionalDocumentJob", this.jobRepository).start(this.translateInstitutionalDocumentStep).next(this.delayStep).build();
    }

    @Bean
    public Job translatePreDepartureResourceJob() {
        return new JobBuilder("translatePreDepartureResourceJob", this.jobRepository).start(this.translatePreDepartureResourceStep).next(this.delayStep).build();
    }

    @Generated
    public TranslationJobConfig(JobRepository jobRepository, Step translateNewsStep, Step translateNewsCategoryStep, Step translateBannerStep, Step translatePhotoGalleryStep, Step translateEventStep, Step translateStaticPageStep, Step translateScholarshipStep, Step translateResearchProjectStep, Step translateWebinarStep, Step translateFaqStep, Step translatePublicationStep, Step translateLeadershipMemberStep, Step translateKeyDateStep, Step translateInstitutionalDocumentStep, Step translatePreDepartureResourceStep, Step delayStep) {
        this.jobRepository = jobRepository;
        this.translateNewsStep = translateNewsStep;
        this.translateNewsCategoryStep = translateNewsCategoryStep;
        this.translateBannerStep = translateBannerStep;
        this.translatePhotoGalleryStep = translatePhotoGalleryStep;
        this.translateEventStep = translateEventStep;
        this.translateStaticPageStep = translateStaticPageStep;
        this.translateScholarshipStep = translateScholarshipStep;
        this.translateResearchProjectStep = translateResearchProjectStep;
        this.translateWebinarStep = translateWebinarStep;
        this.translateFaqStep = translateFaqStep;
        this.translatePublicationStep = translatePublicationStep;
        this.translateLeadershipMemberStep = translateLeadershipMemberStep;
        this.translateKeyDateStep = translateKeyDateStep;
        this.translateInstitutionalDocumentStep = translateInstitutionalDocumentStep;
        this.translatePreDepartureResourceStep = translatePreDepartureResourceStep;
        this.delayStep = delayStep;
    }
}

