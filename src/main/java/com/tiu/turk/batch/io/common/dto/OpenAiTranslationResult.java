package com.tiu.turk.batch.io.common.dto;

import java.util.Map;

public record OpenAiTranslationResult(Map<String, String> translations) {
}
