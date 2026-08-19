package com.tiu.turk.staticpage.web.controller;

import com.tiu.turk.application.common.ApplicationStatus;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.application.web.service.ApplicationWebService;
import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.predeparture.admin.service.PreDepartureResourceService;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.Generated;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/"})
public class ContentPagesWebController {
    private final PathCookieLocaleResolver localeResolver;
    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final ApplicationWebService applicationWebService;
    private final PreDepartureResourceService preDepartureResourceService;
    private final com.tiu.turk.scholarship.web.service.ScholarshipWebService scholarshipWebService;
    private final com.tiu.turk.quota.web.service.OrhunQuotaWebService orhunQuotaWebService;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"/about-us"})
    public String aboutUsPage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("metaDescription", "About the Turkic Universities Union (TURKUNIB): our mission, vision, history and goals in advancing higher education, academic mobility and cooperation across the Turkic world.");
        return "web/single/about-us";
    }

    @GetMapping(value={"/student-council"})
    public String studentCouncilPage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        return "web/single/student-council";
    }

    @GetMapping(value={"/orhun"})
    public String orhunPage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        // Active Orhun call (round): the announcement texts are static, the year and
        // timeline dates come from this record (managed in admin > Orhun Exchange Rounds).
        java.time.LocalDate today = java.time.LocalDate.now();
        com.tiu.turk.scholarship.web.dto.ScholarshipView activeRound = this.scholarshipWebService.getScholarships(locale).stream()
                .filter(s -> s.getDeadline() == null || !s.getDeadline().isBefore(today))
                .findFirst().orElse(null);
        model.addAttribute("activeRound", activeRound);
        model.addAttribute("quotas", activeRound != null ? this.orhunQuotaWebService.getForRound(activeRound.getId()) : java.util.List.of());
        model.addAttribute("metaDescription", "The Orhun Exchange Program enables student and academic staff mobility between member universities of the Turkic Universities Union (TURKUNIB). Learn about eligibility, the application process and scholarships.");
        return "web/single/orhun-process";
    }

    @GetMapping(value={"/membership"})
    public String membershipPage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("metaDescription", "How to become a member of the Turkic Universities Union (TURKUNIB): membership criteria, the application procedure, and guidance on preparing the application file and Rector's Letter.");
        return "web/single/membership";
    }

    @GetMapping(value={"/orhun-guide"})
    public String orhunGuidePage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("metaDescription", "Application guide for the Orhun Exchange Program of the Turkic Universities Union (TURKUNIB): eligibility requirements, application process, required documents and post-selection steps for students and academic staff.");
        return "web/single/orhun-guide";
    }

    @GetMapping(value={"/directive"})
    public String directivePage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream().map(arg_0 -> this.bannerWebMapper.toDto(arg_0)).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        model.addAttribute("metaDescription", "The founding Directive of the Turkic Universities Union (TURKUNIB): purpose, structure, objectives, working principles, membership and the Turkic Higher Education Area, as adopted by the Organization of Turkic States.");
        return "web/single/directive";
    }

    @GetMapping(value={"/cookies"})
    public String cookiesPage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        return "web/single/cookies";
    }

    @GetMapping(value={"/terms-and-conditions"})
    public String termsAndConditionsPage(@PathVariable(value="lang") String lang, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        return "web/single/terms-and-conditions";
    }

    @GetMapping(value={"/student-dashboard"})
    public String studentDashboardPage(@PathVariable(value="lang") String lang, @AuthenticationPrincipal AppUserDetails userDetails, Model model, HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault((String)lang);
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());

        List<ApplicationEntity> applications = this.applicationWebService.getStudentApplications(userDetails.getId());
        long submitted = applications.stream().filter(a -> a.getStatus() == ApplicationStatus.SUBMITTED).count();
        long underReview = applications.stream().filter(a -> a.getStatus() == ApplicationStatus.UNDER_REVIEW
                || a.getStatus() == ApplicationStatus.HOME_APPROVED).count();
        long approved = applications.stream().filter(a -> a.getStatus() == ApplicationStatus.APPROVED
                || a.getStatus() == ApplicationStatus.ACCEPTED).count();
        long rejected = applications.stream().filter(a -> a.getStatus() == ApplicationStatus.REJECTED
                || a.getStatus() == ApplicationStatus.HOME_REJECTED).count();

        model.addAttribute("studentName", userDetails.getFullName());
        model.addAttribute("studentEmail", userDetails.getUsername());
        model.addAttribute("memberSince", userDetails.getCreatedAt());
        model.addAttribute("applications", applications);
        model.addAttribute("totalCount", applications.size());
        model.addAttribute("submittedCount", submitted);
        model.addAttribute("underReviewCount", underReview);
        model.addAttribute("approvedCount", approved);
        model.addAttribute("rejectedCount", rejected);
        model.addAttribute("preDepartureResources", this.preDepartureResourceService.getEnabledResources(locale));
        return "web/single/user-dashboard";
    }

    @Generated
    public ContentPagesWebController(PathCookieLocaleResolver localeResolver, BannerWebService bannerWebService, BannerWebMapper bannerWebMapper, ApplicationWebService applicationWebService, PreDepartureResourceService preDepartureResourceService, com.tiu.turk.scholarship.web.service.ScholarshipWebService scholarshipWebService, com.tiu.turk.quota.web.service.OrhunQuotaWebService orhunQuotaWebService) {
        this.localeResolver = localeResolver;
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.applicationWebService = applicationWebService;
        this.preDepartureResourceService = preDepartureResourceService;
        this.scholarshipWebService = scholarshipWebService;
        this.orhunQuotaWebService = orhunQuotaWebService;
    }
}

