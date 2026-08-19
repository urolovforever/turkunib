package com.tiu.turk.institutionaldoc.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.institutionaldoc.common.InstitutionalDocumentSection;
import com.tiu.turk.institutionaldoc.web.service.InstitutionalDocumentWebService;
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

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}"})
@RequiredArgsConstructor
public class InstitutionalDocumentWebController {
    private final InstitutionalDocumentWebService institutionalDocumentWebService;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final PathCookieLocaleResolver localeResolver;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"/strategic-plan"})
    public String strategicPlan(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        return renderSection(lang, InstitutionalDocumentSection.STRATEGIC_PLAN,
                "Strategic Plan",
                "The strategic direction and long-term plans of the Turkic Universities Union (TURKUNIB).",
                "The strategic plan of the Turkic Universities Union (TURKUNIB).",
                model, req, res);
    }

    @GetMapping(value={"/annual-reports"})
    public String annualReports(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        return renderSection(lang, InstitutionalDocumentSection.ANNUAL_REPORT,
                "Annual Reports",
                "Annual activity reports of the Turkic Universities Union (TURKUNIB).",
                "Annual activity reports of the Turkic Universities Union (TURKUNIB).",
                model, req, res);
    }

    private String renderSection(String lang, InstitutionalDocumentSection section, String heading, String intro,
                                 String metaDescription, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("documents", this.institutionalDocumentWebService.getBySection(section, locale));
        model.addAttribute("heading", heading);
        model.addAttribute("intro", intro);
        model.addAttribute("metaDescription", metaDescription);
        return "web/institutionaldoc/index";
    }
}
