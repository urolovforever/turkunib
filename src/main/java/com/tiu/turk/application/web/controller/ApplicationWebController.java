package com.tiu.turk.application.web.controller;

import com.tiu.turk.application.web.dto.StudentApplicationDto;
import com.tiu.turk.application.web.dto.TeacherApplicationDto;
import com.tiu.turk.application.web.service.ApplicationWebService;
import com.tiu.turk.banner.web.dto.BannerWebIndexDto;
import com.tiu.turk.banner.web.mapper.BannerWebMapper;
import com.tiu.turk.banner.web.service.BannerWebService;
import com.tiu.turk.application.common.Semester;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.configuration.PathCookieLocaleResolver;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.scholarship.web.dto.ScholarshipView;
import com.tiu.turk.scholarship.web.service.ScholarshipWebService;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Orhun Exchange Program application forms (student + teacher).
 * The whole /{lang}/application/** area requires ROLE_STUDENT (portal account).
 */
@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/application"})
public class ApplicationWebController {
    private static final Logger log = LoggerFactory.getLogger(ApplicationWebController.class);

    private final BannerWebService bannerWebService;
    private final BannerWebMapper bannerWebMapper;
    private final ApplicationWebService applicationWebService;
    private final PathCookieLocaleResolver localeResolver;
    private final MemberRepository memberRepository;
    private final ScholarshipWebService scholarshipWebService;
    private final MessageSource messages;

    @ModelAttribute(value="lang")
    public String currentLang(@PathVariable(value="lang") String lang) {
        return lang;
    }

    @GetMapping(value={"/form"})
    public String showStudentForm(@PathVariable(value="lang") String lang,
                                  @RequestParam(value="scholarship", required=false) Long scholarshipId,
                                  Model model, HttpServletRequest req, HttpServletResponse res) {
        prepareFormModel(lang, scholarshipId, model, req, res);
        return "web/application/index";
    }

    @GetMapping(value={"/teacher-form"})
    public String showTeacherForm(@PathVariable(value="lang") String lang,
                                  @RequestParam(value="scholarship", required=false) Long scholarshipId,
                                  Model model, HttpServletRequest req, HttpServletResponse res) {
        prepareFormModel(lang, scholarshipId, model, req, res);
        return "web/application/teacher";
    }

    @PostMapping(value={"/apply"})
    public String submitStudentApplication(@PathVariable(value="lang") String lang,
                                           @AuthenticationPrincipal AppUserDetails userDetails,
                                           @ModelAttribute StudentApplicationDto dto,
                                           @RequestParam(value="transcript", required=false) MultipartFile transcript,
                                           @RequestParam(value="languageCert", required=false) MultipartFile languageCert,
                                           @RequestParam(value="infoForm", required=false) MultipartFile infoForm,
                                           @RequestParam(value="website", required=false) String honeypot,
                                           RedirectAttributes redirectAttributes) {
        if (honeypot != null && !honeypot.isBlank()) {
            return "redirect:/" + lang + "/student-dashboard";
        }
        try {
            this.applicationWebService.createStudentApplication(dto, transcript, languageCert, infoForm, userDetails.getId());
        } catch (Exception e) {
            return failure(e, lang, "/application/form", redirectAttributes);
        }
        redirectAttributes.addFlashAttribute("success", localized("page.apply.success"));
        return "redirect:/" + lang + "/student-dashboard";
    }

    @PostMapping(value={"/teacher-apply"})
    public String submitTeacherApplication(@PathVariable(value="lang") String lang,
                                           @AuthenticationPrincipal AppUserDetails userDetails,
                                           @ModelAttribute TeacherApplicationDto dto,
                                           @RequestParam(value="infoForm", required=false) MultipartFile infoForm,
                                           @RequestParam(value="website", required=false) String honeypot,
                                           RedirectAttributes redirectAttributes) {
        if (honeypot != null && !honeypot.isBlank()) {
            return "redirect:/" + lang + "/student-dashboard";
        }
        try {
            this.applicationWebService.createTeacherApplication(dto, infoForm, userDetails.getId());
        } catch (Exception e) {
            return failure(e, lang, "/application/teacher-form", redirectAttributes);
        }
        redirectAttributes.addFlashAttribute("success", localized("page.apply.success"));
        return "redirect:/" + lang + "/student-dashboard";
    }

    private void prepareFormModel(String lang, Long requestedScholarshipId, Model model,
                                  HttpServletRequest req, HttpServletResponse res) {
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault(lang);
        List<BannerWebIndexDto> banners = this.bannerWebService.getActiveBanners(locale).stream()
                .map(this.bannerWebMapper::toDto).toList();
        this.localeResolver.updateCookieIfChanged(req, res, locale.toJavaLanguageTag());
        model.addAttribute("banners", banners);
        List<com.tiu.turk.member.common.entity.MemberEntity> universities = this.memberRepository.findByEnabledTrueOrderByNameAsc();
        model.addAttribute("universities", universities);
        // Distinct countries of member universities, for the country filter above the university selects.
        model.addAttribute("countries", universities.stream()
                .map(com.tiu.turk.member.common.entity.MemberEntity::getCountry)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toMap(c -> c.getId(), c -> c, (a, b) -> a))
                .values().stream()
                .sorted(java.util.Comparator.comparing(c -> c.getName() == null ? "" : c.getName()))
                .toList());

        // Open Orhun rounds: enabled, application period started and deadline not passed.
        List<ScholarshipView> openRounds = this.scholarshipWebService.getScholarships(locale).stream()
                .filter(ScholarshipView::isOpenNow)
                .toList();
        Long selected = openRounds.stream().map(ScholarshipView::getId)
                .filter(id -> id.equals(requestedScholarshipId)).findFirst().orElse(null);
        if (selected == null && openRounds.size() == 1) {
            selected = openRounds.get(0).getId();
        }
        model.addAttribute("openRounds", openRounds);
        model.addAttribute("selectedScholarshipId", selected);
        model.addAttribute("semesters", Semester.values());
    }

    private String failure(Exception e, String lang, String formPath, RedirectAttributes redirectAttributes) {
        String message = e.getMessage() == null ? "" : e.getMessage();
        String key;
        if (e instanceof IllegalArgumentException && message.startsWith("page.")) {
            key = message;
        } else if (message.startsWith("Unsupported file type")) {
            key = "page.apply.error.filetype";
        } else {
            key = "page.apply.error.generic";
        }
        if ("page.apply.error.generic".equals(key)) {
            log.error("Application submission failed", e);
        }
        redirectAttributes.addFlashAttribute("error", localized(key));
        return "redirect:/" + lang + formPath;
    }

    private String localized(String key) {
        return this.messages.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }

    @Generated
    public ApplicationWebController(BannerWebService bannerWebService, BannerWebMapper bannerWebMapper,
                                    ApplicationWebService applicationWebService, PathCookieLocaleResolver localeResolver,
                                    MemberRepository memberRepository, ScholarshipWebService scholarshipWebService,
                                    MessageSource messages) {
        this.bannerWebService = bannerWebService;
        this.bannerWebMapper = bannerWebMapper;
        this.applicationWebService = applicationWebService;
        this.localeResolver = localeResolver;
        this.memberRepository = memberRepository;
        this.scholarshipWebService = scholarshipWebService;
        this.messages = messages;
    }
}
