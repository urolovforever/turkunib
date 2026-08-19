package com.tiu.turk.index.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import lombok.Generated;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

@Controller
public class RedirectController {
    private final LocaleResolver localeResolver;

    @GetMapping(value={"", "/"})
    public String redirectToHome(HttpServletRequest req) {
        Locale local = this.localeResolver.resolveLocale(req);
        String lang = local.getLanguage();
        return "redirect:/" + lang;
    }

    @Generated
    public RedirectController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
}

