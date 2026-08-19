package com.tiu.turk.research.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.research.common.ResearchProjectStatus;
import com.tiu.turk.research.web.service.ResearchProjectWebService;
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
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/research"})
@RequiredArgsConstructor
public class ResearchProjectWebController {
    private final ResearchProjectWebService researchProjectWebService;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final PathCookieLocaleResolver localeResolver;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"", "/"})
    public String index(@PathVariable(value="lang") String lang,
                        @RequestParam(value="status", required=false) ResearchProjectStatus status,
                        Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("projects", this.researchProjectWebService.getProjects(status, locale));
        model.addAttribute("statuses", ResearchProjectStatus.values());
        model.addAttribute("activeStatus", status);
        model.addAttribute("metaDescription", "Joint research projects and academic collaboration across member universities of the Turkic Universities Union (TURKUNIB).");
        return "web/research/index";
    }

    @GetMapping(value={"/{id}", "/{id}/{slug}"})
    public String detail(@PathVariable(value="lang") String lang, @PathVariable(value="id") Long id,
                         Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        try {
            var project = this.researchProjectWebService.getById(locale, id);
            List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
            this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
            model.addAttribute("banners", banners);
            model.addAttribute("project", project);
            model.addAttribute("metaDescription", project.getTitle() + " — a joint research project of the Turkic Universities Union (TURKUNIB).");
            return "web/research/detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/error/404";
        }
    }
}
