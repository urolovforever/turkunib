package com.tiu.turk.configuration;

import com.tiu.turk.common.enums.TranslationLocale;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

public class PathCookieLocaleResolver
extends CookieLocaleResolver {
    public static final String COOKIE_NAME = "LOCALE";
    public static final TranslationLocale DEFAULT_LOCALE = TranslationLocale.EN;
    private static final Map<String, Locale> LOCALES = Map.of("en", Locale.forLanguageTag("en"), "kg", Locale.forLanguageTag("kg"), "kz", Locale.forLanguageTag("kz"), "tr", Locale.forLanguageTag("tr"), "uz", Locale.forLanguageTag("uz"), "hg", Locale.forLanguageTag("hg"), "az", Locale.forLanguageTag("az"));

    public PathCookieLocaleResolver() {
        super(COOKIE_NAME);
        this.setDefaultLocale(Locale.forLanguageTag(DEFAULT_LOCALE.getCode()));
        this.setCookiePath("/");
    }

    @NonNull
    public Locale resolveLocale(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.length() >= 3 && uri.charAt(0) == '/') {
            String candidate;
            Locale locale;
            int endIndex;
            int secondSlash = uri.indexOf(47, 1);
            if (secondSlash == -1) {
                secondSlash = uri.length();
            }
            if ((endIndex = Math.min(3, secondSlash)) > 1 && (locale = (Locale)LOCALES.get((candidate = uri.substring(1, endIndex)).toLowerCase())) != null) {
                return locale;
            }
        }
        return super.resolveLocale(request);
    }

    public void updateCookieIfChanged(HttpServletRequest req, HttpServletResponse res, Locale locale) {
        Locale current = super.resolveLocale(req);
        if (!current.equals(locale)) {
            super.setLocale(req, res, locale);
        }
    }
}

