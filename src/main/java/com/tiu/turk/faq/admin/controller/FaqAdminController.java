package com.tiu.turk.faq.admin.controller;

import com.tiu.turk.faq.admin.dto.FaqTranslationsForm;
import com.tiu.turk.faq.admin.service.FaqAdminService;
import com.tiu.turk.faq.common.FaqCategory;
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
@RequestMapping(value={"/admin/faq"})
@RequiredArgsConstructor
public class FaqAdminController {
    private final FaqAdminService faqAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("faqs", this.faqAdminService.getAll());
        model.addAttribute("categories", FaqCategory.values());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.faqAdminService.emptyForm());
        }
        return "admin/faq/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") FaqTranslationsForm form,
                         @RequestParam(value="category", required=false) FaqCategory category,
                         @RequestParam(value="displayOrder", required=false) Integer displayOrder,
                         RedirectAttributes redirectAttributes) {
        try {
            this.faqAdminService.createWithTranslations(form, category, displayOrder, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "FAQ added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add FAQ: " + e.getMessage());
        }
        return "redirect:/admin/faq/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("faq", this.faqAdminService.getById(id));
            model.addAttribute("categories", FaqCategory.values());
            return "admin/faq/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/faq/";
        }
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@PathVariable(value="id") Long id,
                         @RequestParam(value="question") String question,
                         @RequestParam(value="answer", required=false) String answer,
                         @RequestParam(value="category", required=false) FaqCategory category,
                         @RequestParam(value="displayOrder", required=false) Integer displayOrder,
                         RedirectAttributes redirectAttributes) {
        try {
            this.faqAdminService.update(id, question, answer, category, displayOrder);
            redirectAttributes.addFlashAttribute("success", "FAQ updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update FAQ: " + e.getMessage());
        }
        return "redirect:/admin/faq/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("faq", this.faqAdminService.getById(id));
            model.addAttribute("form", this.faqAdminService.getTranslationsForm(id));
            model.addAttribute("faqId", id);
            return "admin/faq/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/faq/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") FaqTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.faqAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/faq/translations/" + id;
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.faqAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/faq/";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.faqAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "FAQ deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete FAQ: " + e.getMessage());
        }
        return "redirect:/admin/faq/";
    }
}
