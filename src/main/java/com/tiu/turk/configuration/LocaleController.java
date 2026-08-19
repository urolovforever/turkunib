package com.tiu.turk.configuration;

import com.tiu.turk.common.enums.TranslationLocale;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Lets pages WITHOUT a {lang} path segment (e.g. /login, /register) switch language.
 * Stores the chosen language in the LOCALE cookie using the app code (en/uz/tr/kz/kg/hg/az)
 * so it matches the messages_*.properties bundle naming, then redirects back.
 */
@Controller
@RequiredArgsConstructor
public class LocaleController {
    private final PathCookieLocaleResolver localeResolver;

    @GetMapping(value = {"/set-lang"})
    public String setLang(@RequestParam(value = "lang") String lang,
                          @RequestParam(value = "redirect", required = false) String redirect,
                          HttpServletRequest request, HttpServletResponse response) {
        TranslationLocale loc = TranslationLocale.toLocaleOrDefault(lang);
        this.localeResolver.setLocale(request, response, Locale.forLanguageTag(loc.getCode()));
        // Only allow redirecting back to the auth pages (prevents open redirect).
        String target = "/register".equals(redirect) ? "/register" : "/login";
        return "redirect:" + target;
    }
}
