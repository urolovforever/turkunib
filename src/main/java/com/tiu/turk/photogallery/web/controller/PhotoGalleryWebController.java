package com.tiu.turk.photogallery.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.photogallery.web.dto.PhotoGalleryWebDto;
import com.tiu.turk.photogallery.web.mapper.PhotoGalleryWebMapper;
import com.tiu.turk.photogallery.web.service.PhotoGalleryWebService;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/photos"})
public class PhotoGalleryWebController {
    private final PhotoGalleryWebService photoGalleryWebService;
    private final PhotoGalleryWebMapper photoGalleryWebMapper;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final PathCookieLocaleResolver localeResolver;
    private static final int PHOTO_GALLERIES_LIMIT = 6;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"", "/"})
    public String indexPhotoGallery(@PathVariable(value="lang") String lang, @RequestParam(value="q", required=false) String q, Model model, Pageable pageable, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        Page photoGalleries;
        if (q != null && !q.isBlank()) {
            photoGalleries = this.photoGalleryWebService.searchActivePhotoGalleriesPaginated(locale, q.trim(), pageable.getPageNumber(), 6).map(arg_0 -> this.photoGalleryWebMapper.toDto(arg_0));
        } else {
            photoGalleries = this.photoGalleryWebService.getActivePhotoGalleriesPaginated(locale, pageable.getPageNumber(), 6).map(arg_0 -> this.photoGalleryWebMapper.toDto(arg_0));
        }
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("photoGalleries", photoGalleries);
        model.addAttribute("q", q);
        return "web/photogallery/index";
    }

    @GetMapping(value={"/{photoGalleryId}/{slug}"})
    public String showPhotoGallery(@PathVariable(value="lang") String lang, @PathVariable(value="photoGalleryId") Long photoGalleryId, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        try {
            PhotoGalleryWebDto galleryWebDto = this.photoGalleryWebMapper.toDto(this.photoGalleryWebService.getPhotoGalleryById(locale, photoGalleryId));
            List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
            this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
            model.addAttribute("banners", banners);
            model.addAttribute("photogallery", galleryWebDto);
            return "web/photogallery/show";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/en";
        }
    }

    @Generated
    public PhotoGalleryWebController(PhotoGalleryWebService photoGalleryWebService, PhotoGalleryWebMapper photoGalleryWebMapper, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper, PathCookieLocaleResolver localeResolver) {
        this.photoGalleryWebService = photoGalleryWebService;
        this.photoGalleryWebMapper = photoGalleryWebMapper;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.localeResolver = localeResolver;
    }
}

