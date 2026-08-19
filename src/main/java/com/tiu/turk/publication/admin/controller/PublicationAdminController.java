package com.tiu.turk.publication.admin.controller;

import com.tiu.turk.publication.admin.dto.PublicationTranslationsForm;
import com.tiu.turk.publication.admin.service.PublicationAdminService;
import com.tiu.turk.publication.common.PublicationCategory;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/publications"})
@RequiredArgsConstructor
public class PublicationAdminController {
    private final PublicationAdminService publicationAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("publications", this.publicationAdminService.getAll());
        model.addAttribute("categories", PublicationCategory.values());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.publicationAdminService.emptyForm());
        }
        return "admin/publication/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") PublicationTranslationsForm form,
                         @RequestParam(value="category", required=false) PublicationCategory category,
                         @RequestParam(value="publicationYear", required=false) Integer publicationYear,
                         @RequestParam(value="file") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            this.publicationAdminService.createWithTranslations(form, category, publicationYear, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Publication added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add publication: " + e.getMessage());
        }
        return "redirect:/admin/publications/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("publication", this.publicationAdminService.getById(id));
            model.addAttribute("categories", PublicationCategory.values());
            return "admin/publication/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/publications/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@AuthenticationPrincipal AppUserDetails userDetails,
                         @PathVariable(value="id") Long id,
                         @RequestParam(value="title") String title,
                         @RequestParam(value="description", required=false) String description,
                         @RequestParam(value="category", required=false) PublicationCategory category,
                         @RequestParam(value="publicationYear", required=false) Integer publicationYear,
                         @RequestParam(value="file", required=false) MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            this.publicationAdminService.update(id, title, description, category, publicationYear, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Publication updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update publication: " + e.getMessage());
        }
        return "redirect:/admin/publications/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("publication", this.publicationAdminService.getById(id));
            model.addAttribute("form", this.publicationAdminService.getTranslationsForm(id));
            model.addAttribute("publicationId", id);
            return "admin/publication/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/publications/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") PublicationTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.publicationAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/publications/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.publicationAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/publications/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.publicationAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Publication deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete publication: " + e.getMessage());
        }
        return "redirect:/admin/publications/";
    }
}
