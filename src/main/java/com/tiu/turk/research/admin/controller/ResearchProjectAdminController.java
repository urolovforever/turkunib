package com.tiu.turk.research.admin.controller;

import com.tiu.turk.research.admin.dto.ResearchProjectTranslationsForm;
import com.tiu.turk.research.admin.service.ResearchProjectAdminService;
import com.tiu.turk.research.common.ResearchProjectStatus;
import com.tiu.turk.user.common.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
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
@RequestMapping(value={"/admin/research"})
@RequiredArgsConstructor
public class ResearchProjectAdminController {
    private final ResearchProjectAdminService researchProjectAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("projects", this.researchProjectAdminService.getAll());
        model.addAttribute("statuses", ResearchProjectStatus.values());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.researchProjectAdminService.emptyForm());
        }
        return "admin/research/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") ResearchProjectTranslationsForm form,
                         @RequestParam(value="startYear", required=false) Integer startYear,
                         @RequestParam(value="endYear", required=false) Integer endYear,
                         @RequestParam(value="status", required=false) ResearchProjectStatus status,
                         RedirectAttributes redirectAttributes) {
        try {
            this.researchProjectAdminService.createWithTranslations(form, startYear, endYear, status, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Research project added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add project: " + e.getMessage());
        }
        return "redirect:/admin/research/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("project", this.researchProjectAdminService.getById(id));
            model.addAttribute("statuses", ResearchProjectStatus.values());
            return "admin/research/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/research/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@PathVariable(value="id") Long id,
                         @RequestParam(value="title") String title,
                         @RequestParam(value="subjectArea", required=false) String subjectArea,
                         @RequestParam(value="description", required=false) String description,
                         @RequestParam(value="content", required=false) String content,
                         @RequestParam(value="participatingUniversities", required=false) String participatingUniversities,
                         @RequestParam(value="startYear", required=false) Integer startYear,
                         @RequestParam(value="endYear", required=false) Integer endYear,
                         @RequestParam(value="status", required=false) ResearchProjectStatus status,
                         RedirectAttributes redirectAttributes) {
        try {
            this.researchProjectAdminService.update(id, title, subjectArea, description, content, participatingUniversities, startYear, endYear, status);
            redirectAttributes.addFlashAttribute("success", "Research project updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update project: " + e.getMessage());
        }
        return "redirect:/admin/research/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("project", this.researchProjectAdminService.getById(id));
            model.addAttribute("form", this.researchProjectAdminService.getTranslationsForm(id));
            model.addAttribute("projectId", id);
            return "admin/research/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/research/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") ResearchProjectTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.researchProjectAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/research/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.researchProjectAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/research/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.researchProjectAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Research project deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete project: " + e.getMessage());
        }
        return "redirect:/admin/research/";
    }
}
