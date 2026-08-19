package com.tiu.turk.event.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.event.common.dto.EventSingleTranslationDto;
import com.tiu.turk.event.web.mapper.EventWebMapper;
import com.tiu.turk.event.web.service.EventWebService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/events"})
public class EventWebController {
    private final EventWebService eventWebService;
    private final EventWebMapper eventWebMapper;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final PathCookieLocaleResolver localeResolver;
    @Value(value="${app.yandex.api.key}")
    private String yandexApiKey;
    private static final int EVENTS_LIMIT = 6;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"", "/"})
    public String indexEvents(@PathVariable(value="lang") String lang, Model model, Pageable pageable, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        Page events = this.eventWebService.getActiveEventsPaginated(locale, pageable.getPageNumber(), 6);
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("events", events);
        return "web/event/index";
    }

    @GetMapping(value={"/{eventId}/{slug}"})
    public String showEvent(@PathVariable(value="lang") String lang, @PathVariable(value="eventId") Long eventId, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        try {
            List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
            EventSingleTranslationDto event = this.eventWebService.getEventById(locale, eventId);
            this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
            model.addAttribute("banners", banners);
            model.addAttribute("event", event);
            model.addAttribute("yandexApiKey", this.yandexApiKey);
            model.addAttribute("metaDescription", event.description());
            return "web/event/show";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "web/error/404";
        }
    }

    @Generated
    public EventWebController(EventWebService eventWebService, EventWebMapper eventWebMapper, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper, PathCookieLocaleResolver localeResolver) {
        this.eventWebService = eventWebService;
        this.eventWebMapper = eventWebMapper;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.localeResolver = localeResolver;
    }
}

