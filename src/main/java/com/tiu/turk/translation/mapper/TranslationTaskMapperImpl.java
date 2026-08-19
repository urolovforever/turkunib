package com.tiu.turk.translation.mapper;

import com.tiu.turk.translation.dto.TranslationTaskDto;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import com.tiu.turk.translation.mapper.TranslationTaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TranslationTaskMapperImpl
implements TranslationTaskMapper {
    public TranslationTaskDto toDto(TranslationTaskEntity entity) {
        if (entity == null) {
            return null;
        }
        TranslationTaskDto translationTaskDto = new TranslationTaskDto();
        translationTaskDto.setId(entity.getId());
        if (entity.getModule() != null) {
            translationTaskDto.setModule(entity.getModule().name());
        }
        if (entity.getEntityId() != null) {
            translationTaskDto.setEntityId(String.valueOf(entity.getEntityId()));
        }
        if (entity.getSourceLocale() != null) {
            translationTaskDto.setSourceLocale(entity.getSourceLocale().name());
        }
        if (entity.getTargetLocale() != null) {
            translationTaskDto.setTargetLocale(entity.getTargetLocale().name());
        }
        if (entity.getStatus() != null) {
            translationTaskDto.setStatus(entity.getStatus().name());
        }
        translationTaskDto.setAttempt(entity.getAttempt());
        translationTaskDto.setErrorMessage(entity.getErrorMessage());
        translationTaskDto.setStartedAt(entity.getStartedAt());
        translationTaskDto.setUpdatedAt(entity.getUpdatedAt());
        return translationTaskDto;
    }
}

