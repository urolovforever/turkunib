package com.tiu.turk.translation.entity;

import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.common.enums.TranslationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="translation_tasks")
public class TranslationTaskEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="module_name")
    @Enumerated(value=EnumType.STRING)
    private AppModule module;
    @Column(name="entity_id")
    private Long entityId;
    @Column(name="source_hash")
    private String sourceHash;
    @Column(name="source_locale")
    @Enumerated(value=EnumType.STRING)
    private TranslationLocale sourceLocale;
    @Column(name="target_locale")
    @Enumerated(value=EnumType.STRING)
    private TranslationLocale targetLocale;
    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private TranslationStatus status = TranslationStatus.PENDING;
    @Column(name="attempt")
    private int attempt;
    @Column(name="error_message")
    private String errorMessage;
    @Column(name="started_at")
    private LocalDateTime startedAt;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public AppModule getModule() {
        return this.module;
    }

    @Generated
    public Long getEntityId() {
        return this.entityId;
    }

    @Generated
    public String getSourceHash() {
        return this.sourceHash;
    }

    @Generated
    public TranslationLocale getSourceLocale() {
        return this.sourceLocale;
    }

    @Generated
    public TranslationLocale getTargetLocale() {
        return this.targetLocale;
    }

    @Generated
    public TranslationStatus getStatus() {
        return this.status;
    }

    @Generated
    public int getAttempt() {
        return this.attempt;
    }

    @Generated
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Generated
    public LocalDateTime getStartedAt() {
        return this.startedAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setModule(AppModule module) {
        this.module = module;
    }

    @Generated
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Generated
    public void setSourceHash(String sourceHash) {
        this.sourceHash = sourceHash;
    }

    @Generated
    public void setSourceLocale(TranslationLocale sourceLocale) {
        this.sourceLocale = sourceLocale;
    }

    @Generated
    public void setTargetLocale(TranslationLocale targetLocale) {
        this.targetLocale = targetLocale;
    }

    @Generated
    public void setStatus(TranslationStatus status) {
        this.status = status;
    }

    @Generated
    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    @Generated
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Generated
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

