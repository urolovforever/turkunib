package com.tiu.turk.staticpage.admin.controller;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.admin.dto.StaticPageCreateDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageTranslationDto;
import com.tiu.turk.staticpage.admin.dto.StaticPageUpdateDto;
import com.tiu.turk.staticpage.admin.mapper.StaticPageMapper;
import com.tiu.turk.staticpage.admin.service.StaticPageService;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import com.tiu.turk.user.common.security.AppUserDetails;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/static-pages"})
public class StaticPageController {
    private final StaticPageService staticPageService;
    private final StaticPageMapper staticPageMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/static-pages/index";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page staticPages = this.staticPageService.getAllPages(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.staticPageMapper.toDto(arg_0));
        model.addAttribute("page", staticPages);
        model.addAttribute("q", q);
        return "admin/staticpage/index";
    }

    @GetMapping(value={"/create"})
    public String create(Model model) {
        StaticPageCreateDto createDto = new StaticPageCreateDto();
        Map<TranslationLocale, StaticPageTranslationDto> translations = new EnumMap<TranslationLocale, StaticPageTranslationDto>(TranslationLocale.class);
        for (TranslationLocale locale : TranslationLocale.values()) {
            StaticPageTranslationDto translationDto = new StaticPageTranslationDto();
            translations.put(locale, translationDto);
        }
        createDto.setTranslations(translations);
        model.addAttribute("staticPage", createDto);
        return "admin/staticpage/create";
    }

    @GetMapping(value={"/edit/{staticPageId}"})
    public String edit(@PathVariable(value="staticPageId") Long staticPageId, Model model) {
        try {
            model.addAttribute("staticPage", this.staticPageMapper.toDto(this.staticPageService.getStaticPageById(staticPageId)));
            return "admin/staticpage/edit";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
    }

    @GetMapping(value={"/show/{staticPageId}"})
    public String show(@PathVariable(value="staticPageId") Long staticPageId, Model model) {
        try {
            model.addAttribute("staticPage", this.staticPageMapper.toDto(this.staticPageService.getStaticPageById(staticPageId)));
            return "admin/staticpage/show";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
    }

    @PostMapping(value={"/create"})
    public String createStaticPage(@AuthenticationPrincipal AppUserDetails userDetails, @ModelAttribute(value="staticPage") StaticPageCreateDto createDto, RedirectAttributes redirectAttributes) {
        try {
            StaticPageEntity item = this.staticPageService.saveStaticPage(this.staticPageMapper.toEntity(createDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Static page created successfully.");
            return "redirect:/admin/static-pages/edit/" + item.getId();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/static-pages/create";
        }
    }

    @PostMapping(value={"/update"})
    public String updateStaticPage(@AuthenticationPrincipal AppUserDetails userDetails, @ModelAttribute StaticPageUpdateDto updateDto, RedirectAttributes redirectAttributes) {
        try {
            StaticPageEntity item = this.staticPageService.updateStaticPage(this.staticPageMapper.toEntity(updateDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Static page updated successfully.");
            return "redirect:/admin/static-pages/show/" + item.getId();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/static-pages/edit/" + updateDto.getId();
        }
    }

    @PostMapping(value={"/delete/{staticPageId}"})
    public String deleteStaticPage(@PathVariable(value="staticPageId") Long staticPageId, RedirectAttributes redirectAttributes) {
        try {
            this.staticPageService.deleteStaticPage(staticPageId);
            redirectAttributes.addFlashAttribute("success", "Static page deleted successfully.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @GetMapping(value={"/translate/{staticPageId}"})
    public String translate(@PathVariable(value="staticPageId") Long staticPageId, RedirectAttributes redirectAttributes) {
        try {
            List executedTranslations = this.staticPageService.executeTranslationTasks(staticPageId);
            redirectAttributes.addFlashAttribute("success", ("Translation tasks initialized." + (String)(executedTranslations.isEmpty() ? "" : " Locales: " + String.join((CharSequence)", ", executedTranslations))));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/static-pages/show/" + staticPageId;
    }

    @Generated
    public StaticPageController(StaticPageService staticPageService, StaticPageMapper staticPageMapper) {
        this.staticPageService = staticPageService;
        this.staticPageMapper = staticPageMapper;
    }
}

