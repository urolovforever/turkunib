package com.tiu.turk.news.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.news.web.dto.news.NewsWebDto;
import com.tiu.turk.news.web.dto.news.NewsWebIndexDto;
import com.tiu.turk.news.web.mapper.NewsWebMapper;
import com.tiu.turk.news.web.service.NewsWebService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/news"})
public class NewsWebController {
    private final NewsWebService newsWebService;
    private final NewsWebMapper newsWebMapper;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final PathCookieLocaleResolver localeResolver;
    private static final int NEWS_LIMIT = 8;
    private static final int MORE_NEWS_LIMIT = 4;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"", "/"})
    public String indexPage(@PathVariable(value="lang") String lang, Model model, Pageable pageable, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        Page news = this.newsWebService.getActiveNewsPaginated(locale, pageable.getPageNumber(), 8).map(arg_0 -> this.newsWebMapper.toDto(arg_0));
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("news", news);
        model.addAttribute("metaDescription", "Latest news and announcements from the Turkic Universities Union (TURKUNIB): summits, declarations, events and updates on academic cooperation across the Turkic world.");
        return "web/news/index";
    }

    @GetMapping(value={"/{newsId}/{slug}"})
    public String showNews(@PathVariable(value="lang") String lang, @PathVariable(value="newsId") Long newsId, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        try {
            NewsWebDto news = this.newsWebMapper.toFullDto(this.newsWebService.getNewsById(locale, newsId));
            List<NewsWebIndexDto> moreNews = this.newsWebService.getActiveNews(locale, 4).stream().map(arg_0 -> this.newsWebMapper.toDto(arg_0)).toList();
            this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
            model.addAttribute("news", news);
            model.addAttribute("moreNews", moreNews);
            model.addAttribute("metaDescription", news.description());
            return "web/news/show";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/error/404";
        }
    }

    @Generated
    public NewsWebController(NewsWebService newsWebService, NewsWebMapper newsWebMapper, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper, PathCookieLocaleResolver localeResolver) {
        this.newsWebService = newsWebService;
        this.newsWebMapper = newsWebMapper;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.localeResolver = localeResolver;
    }
}

