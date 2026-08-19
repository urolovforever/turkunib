package com.tiu.turk.institutionaldoc.admin.controller;

import com.tiu.turk.institutionaldoc.admin.dto.InstitutionalDocumentTranslationsForm;
import com.tiu.turk.institutionaldoc.admin.service.InstitutionalDocumentAdminService;
import com.tiu.turk.institutionaldoc.common.InstitutionalDocumentSection;
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
@RequestMapping(value={"/admin/institutional-documents"})
@RequiredArgsConstructor
public class InstitutionalDocumentAdminController {
    private final InstitutionalDocumentAdminService institutionalDocumentAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("documents", this.institutionalDocumentAdminService.getAll());
        model.addAttribute("sections", InstitutionalDocumentSection.values());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.institutionalDocumentAdminService.emptyForm());
        }
        return "admin/institutionaldoc/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") InstitutionalDocumentTranslationsForm form,
                         @RequestParam(value="section") InstitutionalDocumentSection section,
                         @RequestParam(value="documentYear", required=false) Integer documentYear,
                         @RequestParam(value="file") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            this.institutionalDocumentAdminService.createWithTranslations(form, section, documentYear, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Document added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add document: " + e.getMessage());
        }
        return "redirect:/admin/institutional-documents/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("document", this.institutionalDocumentAdminService.getById(id));
            model.addAttribute("sections", InstitutionalDocumentSection.values());
            return "admin/institutionaldoc/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/institutional-documents/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@AuthenticationPrincipal AppUserDetails userDetails,
                         @PathVariable(value="id") Long id,
                         @RequestParam(value="section") InstitutionalDocumentSection section,
                         @RequestParam(value="title") String title,
                         @RequestParam(value="documentYear", required=false) Integer documentYear,
                         @RequestParam(value="description", required=false) String description,
                         @RequestParam(value="file", required=false) MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            this.institutionalDocumentAdminService.update(id, section, title, documentYear, description, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Document updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update document: " + e.getMessage());
        }
        return "redirect:/admin/institutional-documents/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("document", this.institutionalDocumentAdminService.getById(id));
            model.addAttribute("form", this.institutionalDocumentAdminService.getTranslationsForm(id));
            model.addAttribute("documentId", id);
            return "admin/institutionaldoc/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/institutional-documents/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") InstitutionalDocumentTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.institutionalDocumentAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/institutional-documents/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.institutionalDocumentAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/institutional-documents/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.institutionalDocumentAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Document deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete document: " + e.getMessage());
        }
        return "redirect:/admin/institutional-documents/";
    }
}
