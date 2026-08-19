package com.tiu.turk.webinar.admin.controller;

import com.tiu.turk.user.common.security.AppUserDetails;
import com.tiu.turk.webinar.admin.dto.WebinarTranslationsForm;
import com.tiu.turk.webinar.admin.service.WebinarAdminService;
import com.tiu.turk.webinar.common.entity.WebinarEntity;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
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
@RequestMapping(value={"/admin/webinars"})
@RequiredArgsConstructor
public class WebinarAdminController {
    private final WebinarAdminService webinarAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        var webinars = this.webinarAdminService.getAll();
        Map<Long, Long> counts = new LinkedHashMap<>();
        for (WebinarEntity w : webinars) {
            counts.put(w.getId(), this.webinarAdminService.registrationCount(w.getId()));
        }
        model.addAttribute("webinars", webinars);
        model.addAttribute("counts", counts);
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.webinarAdminService.emptyForm());
        }
        return "admin/webinar/index";
    }

    @GetMapping(value={"/registrations/{id}"})
    public String registrations(@PathVariable(value="id") Long id, Model model) {
        model.addAttribute("registrations", this.webinarAdminService.getRegistrations(id));
        model.addAttribute("webinarId", id);
        return "admin/webinar/registrations";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") WebinarTranslationsForm form,
                         @RequestParam(value="startAt") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime startAt,
                         @RequestParam(value="durationMinutes", required=false) Integer durationMinutes,
                         @RequestParam(value="onlineLink", required=false) String onlineLink,
                         @RequestParam(value="capacity", required=false) Integer capacity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.webinarAdminService.createWithTranslations(form, startAt, durationMinutes, onlineLink, capacity, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Webinar added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add webinar: " + e.getMessage());
        }
        return "redirect:/admin/webinars/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("webinar", this.webinarAdminService.getById(id));
            return "admin/webinar/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/webinars/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@PathVariable(value="id") Long id,
                         @RequestParam(value="title") String title,
                         @RequestParam(value="description", required=false) String description,
                         @RequestParam(value="content", required=false) String content,
                         @RequestParam(value="startAt") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime startAt,
                         @RequestParam(value="durationMinutes", required=false) Integer durationMinutes,
                         @RequestParam(value="onlineLink", required=false) String onlineLink,
                         @RequestParam(value="capacity", required=false) Integer capacity,
                         RedirectAttributes redirectAttributes) {
        try {
            this.webinarAdminService.update(id, title, description, content, startAt, durationMinutes, onlineLink, capacity);
            redirectAttributes.addFlashAttribute("success", "Webinar updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update webinar: " + e.getMessage());
        }
        return "redirect:/admin/webinars/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("webinar", this.webinarAdminService.getById(id));
            model.addAttribute("form", this.webinarAdminService.getTranslationsForm(id));
            model.addAttribute("webinarId", id);
            return "admin/webinar/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/webinars/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") WebinarTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.webinarAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/webinars/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.webinarAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/webinars/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.webinarAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Webinar deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete webinar: " + e.getMessage());
        }
        return "redirect:/admin/webinars/";
    }
}
