package com.tiu.turk.webinar.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.webinar.web.service.WebinarWebService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/webinars"})
@RequiredArgsConstructor
public class WebinarWebController {
    private final WebinarWebService webinarWebService;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final PathCookieLocaleResolver localeResolver;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"", "/"})
    public String index(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("webinars", this.webinarWebService.getWebinars(locale));
        model.addAttribute("metaDescription", "Workshops and webinars from the Turkic Universities Union (TURKUNIB). Browse upcoming sessions and register online.");
        return "web/webinar/index";
    }

    @GetMapping(value={"/{id}", "/{id}/{slug}"})
    public String detail(@PathVariable(value="lang") String lang, @PathVariable(value="id") Long id,
                         Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        try {
            var view = this.webinarWebService.getWebinarView(locale, id);
            List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
            this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
            model.addAttribute("banners", banners);
            model.addAttribute("v", view);
            model.addAttribute("metaDescription", view.webinar().getTitle() + " — a workshop/webinar of the Turkic Universities Union (TURKUNIB).");
            return "web/webinar/detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/error/404";
        }
    }

    @PostMapping(value={"/{id}/register"})
    public String register(@PathVariable(value="lang") String lang, @PathVariable(value="id") Long id,
                           @RequestParam(value="fullName") String fullName,
                           @RequestParam(value="email") String email,
                           RedirectAttributes redirectAttributes) {
        try {
            this.webinarWebService.register(id, fullName, email);
            redirectAttributes.addFlashAttribute("regSuccess", "You have successfully registered. See you at the session!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("regError", e.getMessage());
        }
        return "redirect:/" + lang + "/webinars/";
    }
}
