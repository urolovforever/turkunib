package com.tiu.turk.predeparture.admin.controller;

import com.tiu.turk.predeparture.admin.dto.PreDepartureResourceTranslationsForm;
import com.tiu.turk.predeparture.admin.service.PreDepartureResourceService;
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
@RequestMapping(value={"/admin/pre-departure"})
@RequiredArgsConstructor
public class PreDepartureAdminController {
    private final PreDepartureResourceService preDepartureResourceService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("resources", this.preDepartureResourceService.getAllResources());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.preDepartureResourceService.emptyForm());
        }
        return "admin/predeparture/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") PreDepartureResourceTranslationsForm form,
                         @RequestParam(value="file") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            this.preDepartureResourceService.createWithTranslations(form, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Resource added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add resource: " + e.getMessage());
        }
        return "redirect:/admin/pre-departure/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("resource", this.preDepartureResourceService.getById(id));
            return "admin/predeparture/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/pre-departure/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@AuthenticationPrincipal AppUserDetails userDetails,
                         @PathVariable(value="id") Long id,
                         @RequestParam(value="title") String title,
                         @RequestParam(value="description", required=false) String description,
                         @RequestParam(value="file", required=false) MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            this.preDepartureResourceService.update(id, title, description, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Resource updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update resource: " + e.getMessage());
        }
        return "redirect:/admin/pre-departure/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("resource", this.preDepartureResourceService.getById(id));
            model.addAttribute("form", this.preDepartureResourceService.getTranslationsForm(id));
            model.addAttribute("resourceId", id);
            return "admin/predeparture/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/pre-departure/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") PreDepartureResourceTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.preDepartureResourceService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/pre-departure/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.preDepartureResourceService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/pre-departure/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.preDepartureResourceService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Resource deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete resource: " + e.getMessage());
        }
        return "redirect:/admin/pre-departure/";
    }
}
