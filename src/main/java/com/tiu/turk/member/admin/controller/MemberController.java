package com.tiu.turk.member.admin.controller;

import com.tiu.turk.member.admin.dto.country.CountryDto;
import com.tiu.turk.member.admin.dto.country.CountryShortDto;
import com.tiu.turk.member.admin.dto.member.MemberCreateDto;
import com.tiu.turk.member.admin.dto.member.MemberUpdateDto;
import com.tiu.turk.member.admin.mapper.CountryMapper;
import com.tiu.turk.member.admin.mapper.MemberMapper;
import com.tiu.turk.member.admin.service.CountryService;
import com.tiu.turk.member.admin.service.MemberService;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.validation.Valid;
import java.util.stream.Stream;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping(value={"/admin/member"})
public class MemberController {
    @Value(value="${app.yandex.api.key}")
    private String yandexApiKey;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final CountryService countryService;
    private final CountryMapper countryMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/member/index";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getAllMembers(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page members = this.memberService.getAllMembers(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.memberMapper.toDto(arg_0));
        model.addAttribute("page", members);
        model.addAttribute("q", q);
        return "admin/member/index";
    }

    @GetMapping(value={"/create"})
    public String createMember(Model model) {
        Stream<CountryShortDto> countries = this.countryService.getAllCountries().stream().map(arg_0 -> this.countryMapper.toShortDto(arg_0));
        if (!model.containsAttribute("member")) {
            model.addAttribute("member", new MemberCreateDto());
        }
        model.addAttribute("countries", countries);
        model.addAttribute("yandexApiKey", this.yandexApiKey);
        return "admin/member/create";
    }

    @GetMapping(value={"/edit/{memberId}"})
    public String editMember(@PathVariable(value="memberId") Long memberId, Model model) {
        try {
            Stream<CountryShortDto> countries = this.countryService.getAllCountries().stream().map(arg_0 -> this.countryMapper.toShortDto(arg_0));
            if (!model.containsAttribute("member")) {
                model.addAttribute("member", new MemberUpdateDto());
            }
            model.addAttribute("countries", countries);
            model.addAttribute("member", this.memberMapper.toDto(this.memberService.getMemberById(memberId)));
            model.addAttribute("yandexApiKey", this.yandexApiKey);
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/member/edit";
    }

    @GetMapping(value={"/show/{memberId}"})
    public String showMember(@PathVariable(value="memberId") Long memberId, Model model) {
        try {
            model.addAttribute("member", this.memberMapper.toDto(this.memberService.getMemberById(memberId)));
            model.addAttribute("yandexApiKey", this.yandexApiKey);
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/member/show";
    }

    @PostMapping(value={"/create"})
    public String createMember(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="member") MemberCreateDto createDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("member", createDto);
            model.addAttribute("countries", this.countryService.getAllCountries().stream().map(arg_0 -> this.countryMapper.toShortDto(arg_0)));
            model.addAttribute("yandexApiKey", this.yandexApiKey);
            return "admin/member/create";
        }
        try {
            MemberEntity entity = this.memberService.createMember(file, this.memberMapper.toEntity(createDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Member created successfully");
            return "redirect:/admin/member/edit/" + entity.getId();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/member/create";
        }
    }

    @PostMapping(value={"/update"})
    public String updateMember(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="member") MemberUpdateDto updateDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            updateDto.setCountry(new CountryDto(updateDto.getCountryId()));
            model.addAttribute("member", updateDto);
            model.addAttribute("countries", this.countryService.getAllCountries().stream().map(arg_0 -> this.countryMapper.toShortDto(arg_0)));
            model.addAttribute("yandexApiKey", this.yandexApiKey);
            return "admin/member/edit";
        }
        try {
            MemberEntity entity = this.memberService.updateMember(updateDto.getId(), file, this.memberMapper.toEntity(updateDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Member updated successfully");
            return "redirect:/admin/member/show/" + entity.getId();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/member/edit/" + updateDto.getId();
        }
    }

    @PostMapping(value={"/delete/{memberId}"})
    public String deleteMember(@PathVariable Long memberId, RedirectAttributes redirectAttributes) {
        try {
            this.memberService.deleteMember(memberId);
            redirectAttributes.addFlashAttribute("success", "Member deleted successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @Generated
    public MemberController(MemberService memberService, MemberMapper memberMapper, CountryService countryService, CountryMapper countryMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }
}

