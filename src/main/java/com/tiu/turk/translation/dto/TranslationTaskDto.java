package com.tiu.turk.translation.dto;

import java.time.LocalDateTime;
import lombok.Generated;

public class TranslationTaskDto {
    private Long id;
    private String module;
    private String entityId;
    private String sourceLocale;
    private String targetLocale;
    private String status;
    private int attempt;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getModule() {
        return this.module;
    }

    @Generated
    public String getEntityId() {
        return this.entityId;
    }

    @Generated
    public String getSourceLocale() {
        return this.sourceLocale;
    }

    @Generated
    public String getTargetLocale() {
        return this.targetLocale;
    }

    @Generated
    public String getStatus() {
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
    public void setModule(String module) {
        this.module = module;
    }

    @Generated
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Generated
    public void setSourceLocale(String sourceLocale) {
        this.sourceLocale = sourceLocale;
    }

    @Generated
    public void setTargetLocale(String targetLocale) {
        this.targetLocale = targetLocale;
    }

    @Generated
    public void setStatus(String status) {
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

