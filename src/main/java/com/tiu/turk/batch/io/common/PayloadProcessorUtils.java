package com.tiu.turk.batch.io.common;

import com.tiu.turk.batch.io.common.MetaType;
import java.util.Map;

public final class PayloadProcessorUtils {
    private PayloadProcessorUtils() {
    }

    public static void putIfNotBlank(Map<String, String> map, String key, String value) {
        String trimmed;
        if (value != null && !(trimmed = value.trim()).isEmpty()) {
            map.put(key, value);
        }
    }

    public static void putIfNotNull(Map<String, MetaType> map, String key, MetaType value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}

