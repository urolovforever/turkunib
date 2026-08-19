package com.tiu.turk.staticpage.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.staticpage.common.dto.StaticPageSingleTranslationDto;
import com.tiu.turk.staticpage.web.service.StaticPageWebService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/"})
public class StaticPageWebController {
    private final StaticPageWebService staticPageWebService;
    private final PathCookieLocaleResolver localeResolver;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;

    @GetMapping(value={"{pageName}"})
    public String showStaticPage(@PathVariable(value="lang") String lang, @PathVariable(value="pageName") String pageName, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        StaticPageSingleTranslationDto staticPage = this.staticPageWebService.getStaticPageBySlug(pageName, locale);
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("staticPage", staticPage);
        return "web/staticpage/show";
    }

    @Generated
    public StaticPageWebController(StaticPageWebService staticPageWebService, PathCookieLocaleResolver localeResolver, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper) {
        this.staticPageWebService = staticPageWebService;
        this.localeResolver = localeResolver;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
    }
}

