package com.tiu.turk.search;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = {"/{lang:en|tr|kz|kg|uz|hg|az}/search"})
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final PathCookieLocaleResolver localeResolver;

    @ModelAttribute(value = "lang")
    public String currentLang(@PathVariable(value = "lang") String lang) {
        return lang;
    }

    @GetMapping(value = {"", "/"})
    public String search(@PathVariable(value = "lang") String lang,
                         @RequestParam(value = "q", required = false) String q,
                         Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault(lang);
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        List<SearchResult> results = this.searchService.search(lang, q);
        model.addAttribute("query", q == null ? "" : q.trim());
        model.addAttribute("results", results);
        model.addAttribute("metaDescription", "Search the Turkic Universities Union (TURKUNIB) website.");
        return "web/search/index";
    }
}
