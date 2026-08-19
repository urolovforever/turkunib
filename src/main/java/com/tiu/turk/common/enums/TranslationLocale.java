package com.tiu.turk.common.enums;

import java.util.List;
import java.util.Locale;
import lombok.Generated;

public enum TranslationLocale {
    EN("English", "en", "en"),
    KG("Kyrgyz", "kg", "ky"),
    KZ("Kazakh", "kz", "kk"),
    TR("Turkish", "tr", "tr"),
    UZ("Uzbek", "uz", "uz"),
    HG("Hungarian", "hg", "hu"),
    AZ("Azerbaijani", "az", "az");

    private final String language;
    private final String code;
    private final String isoCode;

    public static List<TranslationLocale> defaultTargetLocales() {
        return List.of(KG, KZ, TR, UZ, HG, AZ);
    }

    private TranslationLocale(String language, String code, String isoCode) {
        this.language = language;
        this.code = code;
        this.isoCode = isoCode;
    }

    public static TranslationLocale toLocaleOrDefault(String code) {
        if (code == null || code.isBlank()) {
            return EN;
        }
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (!locale.getCode().equalsIgnoreCase(code)) continue;
            return locale;
        }
        return EN;
    }

    public Locale toJavaLanguageTag() {
        return Locale.forLanguageTag(this.isoCode);
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public String getIsoCode() {
        return this.isoCode;
    }
}

