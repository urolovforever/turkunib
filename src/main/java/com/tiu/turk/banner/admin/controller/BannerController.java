package com.tiu.turk.banner.admin.controller;

import com.tiu.turk.banner.admin.dto.BannerCreateDto;
import com.tiu.turk.banner.admin.dto.BannerTranslationUpdateDto;
import com.tiu.turk.banner.admin.dto.BannerUpdateDto;
import com.tiu.turk.banner.admin.mapper.BannerMapper;
import com.tiu.turk.banner.admin.service.BannerService;
import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/banner"})
public class BannerController {
    private final BannerService bannerService;
    private final BannerMapper bannerMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/banner/index";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getAllBanners(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page page = this.bannerService.getAllBanners(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.bannerMapper.toDto(arg_0));
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "admin/banner/index";
    }

    @GetMapping(value={"/create"})
    public String createBanner(Model model) {
        if (!model.containsAttribute("banner")) {
            BannerCreateDto createDto = new BannerCreateDto();
            Map<String, BannerTranslationUpdateDto> translations = new LinkedHashMap<>();
            for (TranslationLocale locale : TranslationLocale.values()) {
                translations.put(locale.name(), new BannerTranslationUpdateDto());
            }
            createDto.setTranslations(translations);
            model.addAttribute("banner", createDto);
        }
        return "admin/banner/create";
    }

    @GetMapping(value={"/edit/{bannerId}"})
    public String editBanner(@PathVariable(value="bannerId") Long bannerId, Model model) {
        try {
            model.addAttribute("banner", this.bannerMapper.toDto(this.bannerService.getBannerById(bannerId)));
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/banner/edit";
    }

    @GetMapping(value={"/show/{bannerId}"})
    public String showBanner(@PathVariable(value="bannerId") Long bannerId, Model model) {
        try {
            model.addAttribute("banner", this.bannerMapper.toDto(this.bannerService.getBannerById(bannerId)));
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/banner/show";
    }

    @PostMapping(value={"/create"})
    public String createBanner(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="banner") BannerCreateDto createDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("banner", createDto);
            return "admin/banner/create";
        }
        try {
            BannerEntity entity = this.bannerService.createBanner(file, this.bannerMapper.toEntity(createDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Banner created successfully");
            return "redirect:/admin/banner/edit/" + entity.getId();
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("banner", createDto);
            return "admin/banner/create";
        }
    }

    @PostMapping(value={"/update"})
    public String updateBanner(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="banner") BannerUpdateDto updateDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("banner", updateDto);
            return "admin/banner/edit";
        }
        try {
            BannerEntity entity = this.bannerService.updateBanner(updateDto.getId(), file, this.bannerMapper.toEntity(updateDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Banner updated successfully");
            return "redirect:/admin/banner/show/" + entity.getId();
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("banner", updateDto);
            return "admin/banner/edit";
        }
    }

    @PostMapping(value={"/delete/{bannerId}"})
    public String deleteBanner(@PathVariable Long bannerId, RedirectAttributes redirectAttributes) {
        try {
            this.bannerService.deleteBanner(bannerId);
            redirectAttributes.addFlashAttribute("success", "Banner deleted successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @GetMapping(value={"/translate/{bannerId}"})
    public String translateBanner(@PathVariable Long bannerId, RedirectAttributes redirectAttributes) {
        try {
            List executedTranslations = this.bannerService.executeTranslationTasks(bannerId);
            redirectAttributes.addFlashAttribute("success", ("Translation tasks initialized." + (String)(executedTranslations.isEmpty() ? "" : " Locales: " + String.join((CharSequence)", ", executedTranslations))));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/banner/show/" + bannerId;
    }

    @Generated
    public BannerController(BannerService bannerService, BannerMapper bannerMapper) {
        this.bannerService = bannerService;
        this.bannerMapper = bannerMapper;
    }
}

