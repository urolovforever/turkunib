package com.tiu.turk.scholarship.admin.controller;

import com.tiu.turk.scholarship.admin.dto.ScholarshipTranslationsForm;
import com.tiu.turk.scholarship.admin.service.ScholarshipAdminService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/scholarships"})
@RequiredArgsConstructor
public class ScholarshipAdminController {
    private final ScholarshipAdminService scholarshipAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("scholarships", this.scholarshipAdminService.getAll());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.scholarshipAdminService.emptyForm());
        }
        return "admin/scholarship/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") ScholarshipTranslationsForm form,
                         @RequestParam(value="applicationStart", required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate applicationStart,
                         @RequestParam(value="deadline", required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate deadline,
                         @RequestParam(value="applyUrl", required=false) String applyUrl,
                         @RequestParam(value="displayOrder", required=false) Integer displayOrder,
                         @RequestParam(value="document", required=false) MultipartFile document,
                         RedirectAttributes redirectAttributes) {
        try {
            this.scholarshipAdminService.createWithTranslations(form, applicationStart, deadline, applyUrl, displayOrder, document, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Round added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add round: " + e.getMessage());
        }
        return "redirect:/admin/scholarships/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("scholarship", this.scholarshipAdminService.getById(id));
            return "admin/scholarship/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/scholarships/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@PathVariable(value="id") Long id,
                         @AuthenticationPrincipal AppUserDetails userDetails,
                         @RequestParam(value="title") String title,
                         @RequestParam(value="description", required=false) String description,
                         @RequestParam(value="note", required=false) String note,
                         @RequestParam(value="applicationStart", required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate applicationStart,
                         @RequestParam(value="deadline", required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate deadline,
                         @RequestParam(value="displayOrder", required=false) Integer displayOrder,
                         @ModelAttribute com.tiu.turk.scholarship.admin.dto.RoundDatesForm dates,
                         RedirectAttributes redirectAttributes) {
        try {
            this.scholarshipAdminService.update(id, title, description, note, applicationStart, deadline, displayOrder, dates, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Round updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update round: " + e.getMessage());
        }
        return "redirect:/admin/scholarships/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("scholarship", this.scholarshipAdminService.getById(id));
            model.addAttribute("form", this.scholarshipAdminService.getTranslationsForm(id));
            model.addAttribute("scholarshipId", id);
            return "admin/scholarship/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/scholarships/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") ScholarshipTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.scholarshipAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/scholarships/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.scholarshipAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/scholarships/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.scholarshipAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Scholarship deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete scholarship: " + e.getMessage());
        }
        return "redirect:/admin/scholarships/";
    }
}
