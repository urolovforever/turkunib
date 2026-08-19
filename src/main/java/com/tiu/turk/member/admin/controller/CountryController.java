package com.tiu.turk.member.admin.controller;

import com.tiu.turk.member.admin.dto.country.CountryCreateDto;
import com.tiu.turk.member.admin.dto.country.CountryUpdateDto;
import com.tiu.turk.member.admin.mapper.CountryMapper;
import com.tiu.turk.member.admin.service.CountryService;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.validation.Valid;
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
@RequestMapping(value={"/admin/member/country"})
public class CountryController {
    private final CountryService countryService;
    private final CountryMapper countryMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/member/country/";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getAllCountries(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page page = this.countryService.getAllCountries(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.countryMapper.toDto(arg_0));
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "admin/member/country/index";
    }

    @PostMapping(value={"/create"})
    public String createCountry(@AuthenticationPrincipal AppUserDetails appUserDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute CountryCreateDto createDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMsg.append(error.getDefaultMessage()).append(" "));
            redirectAttributes.addFlashAttribute("error", errorMsg.toString());
            return "redirect:/admin/member/country/index";
        }
        try {
            this.countryService.createCountry(file, this.countryMapper.toEntity(createDto), appUserDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Country created successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @PostMapping(value={"/update"})
    public String updateCountry(@AuthenticationPrincipal AppUserDetails appUserDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute CountryUpdateDto updateDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMsg.append(error.getDefaultMessage()).append(" "));
            redirectAttributes.addFlashAttribute("error", errorMsg.toString());
            return "redirect:/admin/member/country/index";
        }
        try {
            this.countryService.updateCountry(updateDto.getId(), file, this.countryMapper.toEntity(updateDto), appUserDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Country updated successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @PostMapping(value={"/delete/{countryId}"})
    public String deleteCountry(@PathVariable Long countryId, RedirectAttributes redirectAttributes) {
        try {
            this.countryService.deleteCountry(countryId);
            redirectAttributes.addFlashAttribute("success", "Country deleted successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @Generated
    public CountryController(CountryService countryService, CountryMapper countryMapper) {
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }
}

