package com.tiu.turk.contacts.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.contacts.web.dto.FeedbackCreateDto;
import com.tiu.turk.contacts.web.mapper.FeedbackMapper;
import com.tiu.turk.contacts.web.service.ContactsWebService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/contact-us"})
public class ContactsWebController {
    private final PathCookieLocaleResolver localeResolver;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final ContactsWebService contactsWebService;
    private final FeedbackMapper feedbackMapper;
    @Value(value="${app.yandex.api.key}")
    private String yandexApiKey;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"", "/"})
    public String contactUsPage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("yandexApiKey", this.yandexApiKey);
        return "web/single/contacts";
    }

    @PostMapping(value={"", "/"})
    public String submitContactForm(@PathVariable(value="lang") String lang, @ModelAttribute FeedbackCreateDto createDto, RedirectAttributes redirectAttributes) {
        try {
            this.contactsWebService.saveFeedback(this.feedbackMapper.toEntity(createDto));
            redirectAttributes.addFlashAttribute("success", "contact.submission.success");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "contact.submission.error");
        }
        return "redirect:/" + lang + "/contact-us";
    }

    @Generated
    public ContactsWebController(PathCookieLocaleResolver localeResolver, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper, ContactsWebService contactsWebService, FeedbackMapper feedbackMapper) {
        this.localeResolver = localeResolver;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.contactsWebService = contactsWebService;
        this.feedbackMapper = feedbackMapper;
    }
}

