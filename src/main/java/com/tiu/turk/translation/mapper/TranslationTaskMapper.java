package com.tiu.turk.translation.mapper;

import com.tiu.turk.translation.dto.TranslationTaskDto;
import com.tiu.turk.translation.entity.TranslationTaskEntity;

public interface TranslationTaskMapper {
    public TranslationTaskDto toDto(TranslationTaskEntity var1);
}

