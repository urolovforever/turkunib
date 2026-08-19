package com.tiu.turk.batch.io.common.dto;

import com.tiu.turk.batch.io.common.MetaType;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import java.util.Map;

public record SourcePayload(Long entityId, Long taskId, String sourceHash, AppModule module, TranslationLocale sourceLocale, TranslationLocale targetLocale, Map<String, String> fieldsData, Map<String, MetaType> fieldsMetaData) {
}
