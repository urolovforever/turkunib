package com.tiu.turk.batch.io.common.dto;

import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import java.util.Map;

public record TranslationResult(Long taskId, Long entityId, String sourceHash, AppModule module, TranslationLocale sourceLocale, TranslationLocale targetLocale, Map<String, String> translatedFields) {
}
