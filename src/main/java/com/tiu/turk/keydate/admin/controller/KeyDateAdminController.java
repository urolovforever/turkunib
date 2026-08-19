package com.tiu.turk.keydate.admin.controller;

import com.tiu.turk.keydate.admin.dto.KeyDateTranslationsForm;
import com.tiu.turk.keydate.admin.service.KeyDateAdminService;
import com.tiu.turk.keydate.common.KeyDateType;
import com.tiu.turk.user.common.security.AppUserDetails;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping(value={"/admin/key-dates"})
@RequiredArgsConstructor
public class KeyDateAdminController {
    private final KeyDateAdminService keyDateAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("keyDates", this.keyDateAdminService.getAll());
        model.addAttribute("types", KeyDateType.values());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.keyDateAdminService.emptyForm());
        }
        return "admin/keydate/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") KeyDateTranslationsForm form,
                         @RequestParam(value="eventDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate eventDate,
                         @RequestParam(value="endDate", required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam(value="type", required=false) KeyDateType type,
                         RedirectAttributes redirectAttributes) {
        try {
            this.keyDateAdminService.createWithTranslations(form, eventDate, endDate, type, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Date added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add date: " + e.getMessage());
        }
        return "redirect:/admin/key-dates/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("keyDate", this.keyDateAdminService.getById(id));
            model.addAttribute("types", KeyDateType.values());
            return "admin/keydate/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/key-dates/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@PathVariable(value="id") Long id,
                         @RequestParam(value="title") String title,
                         @RequestParam(value="eventDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate eventDate,
                         @RequestParam(value="endDate", required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam(value="description", required=false) String description,
                         @RequestParam(value="type", required=false) KeyDateType type,
                         RedirectAttributes redirectAttributes) {
        try {
            this.keyDateAdminService.update(id, title, eventDate, endDate, description, type);
            redirectAttributes.addFlashAttribute("success", "Date updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update date: " + e.getMessage());
        }
        return "redirect:/admin/key-dates/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("keyDate", this.keyDateAdminService.getById(id));
            model.addAttribute("form", this.keyDateAdminService.getTranslationsForm(id));
            model.addAttribute("keyDateId", id);
            return "admin/keydate/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/key-dates/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") KeyDateTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.keyDateAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/key-dates/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.keyDateAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/key-dates/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.keyDateAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Date deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete date: " + e.getMessage());
        }
        return "redirect:/admin/key-dates/";
    }
}
