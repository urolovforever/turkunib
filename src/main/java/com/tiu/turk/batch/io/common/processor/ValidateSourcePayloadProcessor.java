package com.tiu.turk.batch.io.common.processor;

import com.tiu.turk.batch.io.common.dto.SourcePayload;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ValidateSourcePayloadProcessor
implements ItemProcessor<SourcePayload, SourcePayload> {
    public SourcePayload process(SourcePayload item) throws Exception {
        if (item == null) {
            throw new IllegalStateException("NewsSourcePayload is null");
        }
        if (item.fieldsData().isEmpty()) {
            throw new IllegalStateException("Fields data is empty for entityId: " + item.entityId() + ", targetLocale: " + String.valueOf(item.targetLocale()));
        }
        item.fieldsData().forEach((field, value) -> {
            if (this.isNullOrEmpty(value)) {
                throw new IllegalStateException("Field '" + field + "' is null or empty for entityId: " + item.entityId() + ", targetLocale: " + String.valueOf(item.targetLocale()));
            }
        });
        return item;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isBlank();
    }
}

