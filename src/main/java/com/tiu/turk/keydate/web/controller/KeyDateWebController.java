package com.tiu.turk.keydate.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.keydate.web.service.KeyDateWebService;
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
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/calendar"})
@RequiredArgsConstructor
public class KeyDateWebController {
    private final KeyDateWebService keyDateWebService;
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
        model.addAttribute("keyDates", this.keyDateWebService.getKeyDates(locale));
        model.addAttribute("metaDescription", "Academic calendar and key dates of the Turkic Universities Union (TURKUNIB): application deadlines, summits and important events.");
        return "web/keydate/index";
    }
}
