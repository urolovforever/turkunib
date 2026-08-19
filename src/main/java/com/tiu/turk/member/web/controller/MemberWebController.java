package com.tiu.turk.member.web.controller;

import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.member.web.dto.CountryWebDto;
import com.tiu.turk.member.web.dto.MemberWebDto;
import com.tiu.turk.member.web.mapper.CountryWebMapper;
import com.tiu.turk.member.web.mapper.MemberWebMapper;
import com.tiu.turk.member.web.service.MemberWebService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/members"})
public class MemberWebController {
    private final MemberWebService memberWebService;
    private final MemberWebMapper memberWebMapper;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final CountryWebMapper countryWebMapper;
    private final PathCookieLocaleResolver localeResolver;
    @Value(value="${app.yandex.api.key}")
    private String yandexApiKey;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"", "/"})
    public String showMember(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        List<CountryWebDto> membersCountries = this.memberWebService.getAllCountries().stream().map(arg_0 -> this.countryWebMapper.toDto(arg_0)).toList();
        Map membersByCountry = this.memberWebMapper.toDtoMap(this.memberWebService.getAllMembersMappedByCountryId());
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("countries", membersCountries);
        model.addAttribute("membersByCountry", membersByCountry);
        model.addAttribute("yandexApiKey", this.yandexApiKey);
        model.addAttribute("metaDescription", "Member universities of the Turkic Universities Union (TURKUNIB) across Azerbaijan, Kazakhstan, Kyrgyzstan, Turkiye, Uzbekistan, Hungary and beyond. Browse the universities partnering in the Orhun Exchange Program.");
        return "web/member/show";
    }

    @GetMapping(value={"/{memberId}", "/{memberId}/{slug}"})
    public String showMemberDetail(@PathVariable(value="lang") String lang, @PathVariable(value="memberId") Long memberId, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        try {
            MemberWebDto member = this.memberWebMapper.toDto(this.memberWebService.getMemberById(memberId));
            List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
            this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
            model.addAttribute("banners", banners);
            model.addAttribute("member", member);
            model.addAttribute("yandexApiKey", this.yandexApiKey);
            model.addAttribute("metaDescription", member.name() + " — a member university of the Turkic Universities Union (TURKUNIB)" + (member.country() != null ? ", " + member.country().name() : "") + ". Founded, programs and contact details.");
            return "web/member/detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/error/404";
        }
    }

    @Generated
    public MemberWebController(MemberWebService memberWebService, MemberWebMapper memberWebMapper, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper, CountryWebMapper countryWebMapper, PathCookieLocaleResolver localeResolver) {
        this.memberWebService = memberWebService;
        this.memberWebMapper = memberWebMapper;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.countryWebMapper = countryWebMapper;
        this.localeResolver = localeResolver;
    }
}

