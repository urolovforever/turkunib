package com.tiu.turk.index.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.web.mapper.EventWebMapper;
import com.tiu.turk.event.web.service.EventWebService;
import com.tiu.turk.member.web.dto.CountryWebDto;
import com.tiu.turk.member.web.mapper.CountryWebMapper;
import com.tiu.turk.member.web.mapper.MemberWebMapper;
import com.tiu.turk.member.web.service.MemberWebService;
import com.tiu.turk.news.web.dto.news.NewsWebIndexDto;
import com.tiu.turk.news.web.mapper.NewsWebMapper;
import com.tiu.turk.news.web.service.NewsWebService;
import com.tiu.turk.photogallery.web.dto.PhotoGalleryWebDto;
import com.tiu.turk.photogallery.web.mapper.PhotoGalleryWebMapper;
import com.tiu.turk.photogallery.web.service.PhotoGalleryWebService;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}"})
public class IndexController {
    private final MemberWebMapper memberWebMapper;
    @Value(value="${app.yandex.api.key}")
    private String yandexApiKey;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final NewsWebService newsWebService;
    private final NewsWebMapper newsWebMapper;
    private final MemberWebService memberWebService;
    private final CountryWebMapper countryWebMapper;
    private final EventWebService eventWebService;
    private final EventWebMapper eventWebMapper;
    private final PhotoGalleryWebService photoGalleryWebService;
    private final PhotoGalleryWebMapper photoGalleryWebMapper;
    private static final int NEWS_LIMIT = 12;
    private static final int EVENTS_LIMIT = 3;
    private static final int PHOTO_GALLERIES_LIMIT = 3;

    @GetMapping(value={"", "/"})
    public String indexPage(@PathVariable(value="lang") String lang, Model model) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        List<NewsWebIndexDto> news = this.newsWebService.getActiveNews(locale, 12).stream().map(arg_0 -> this.newsWebMapper.toDto(arg_0)).toList();
        List<CountryWebDto> membersCountries = this.memberWebService.getAllCountries().stream().map(arg_0 -> this.countryWebMapper.toDto(arg_0)).toList();
        Map membersByCountry = this.memberWebMapper.toDtoMap(this.memberWebService.getAllMembersMappedByCountryId());
        List events = this.eventWebService.getActiveEvents(locale, 3);
        List<PhotoGalleryWebDto> photoGalleries = this.photoGalleryWebService.getActivePhotoGalleries(locale, 3).stream().map(arg_0 -> this.photoGalleryWebMapper.toDto(arg_0)).toList();
        model.addAttribute("banners", banners);
        model.addAttribute("news", news);
        model.addAttribute("lang", lang);
        model.addAttribute("countries", membersCountries);
        model.addAttribute("membersByCountry", membersByCountry);
        model.addAttribute("events", events);
        model.addAttribute("photoGalleries", photoGalleries);
        model.addAttribute("yandexApiKey", this.yandexApiKey);
        model.addAttribute("metaDescription", "TURKUNIB, the Turkic Universities Union, unites leading universities of the Turkic world. Explore the Orhun Exchange Program, member universities, news and events.");
        return "web/index/index";
    }

    @Generated
    public IndexController(MemberWebMapper memberWebMapper, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper, NewsWebService newsWebService, NewsWebMapper newsWebMapper, MemberWebService memberWebService, CountryWebMapper countryWebMapper, EventWebService eventWebService, EventWebMapper eventWebMapper, PhotoGalleryWebService photoGalleryWebService, PhotoGalleryWebMapper photoGalleryWebMapper) {
        this.memberWebMapper = memberWebMapper;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.newsWebService = newsWebService;
        this.newsWebMapper = newsWebMapper;
        this.memberWebService = memberWebService;
        this.countryWebMapper = countryWebMapper;
        this.eventWebService = eventWebService;
        this.eventWebMapper = eventWebMapper;
        this.photoGalleryWebService = photoGalleryWebService;
        this.photoGalleryWebMapper = photoGalleryWebMapper;
    }
}

