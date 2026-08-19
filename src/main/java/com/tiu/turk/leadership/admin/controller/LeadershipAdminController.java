package com.tiu.turk.leadership.admin.controller;

import com.tiu.turk.leadership.admin.dto.LeadershipMemberTranslationsForm;
import com.tiu.turk.leadership.admin.service.LeadershipAdminService;
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
@RequestMapping(value={"/admin/leadership"})
@RequiredArgsConstructor
public class LeadershipAdminController {
    private final LeadershipAdminService leadershipAdminService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("members", this.leadershipAdminService.getAll());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", this.leadershipAdminService.emptyForm());
        }
        return "admin/leadership/index";
    }

    @PostMapping(value={"/create"})
    public String create(@AuthenticationPrincipal AppUserDetails userDetails,
                         @ModelAttribute(value="form") LeadershipMemberTranslationsForm form,
                         @RequestParam(value="fullName") String fullName,
                         @RequestParam(value="displayOrder", required=false) Integer displayOrder,
                         @RequestParam(value="photo", required=false) MultipartFile photo,
                         RedirectAttributes redirectAttributes) {
        try {
            this.leadershipAdminService.createWithTranslations(form, fullName, displayOrder, photo, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Member added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add member: " + e.getMessage());
        }
        return "redirect:/admin/leadership/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("member", this.leadershipAdminService.getById(id));
            return "admin/leadership/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/leadership/";
        }
    }

    @GetMapping(value={"/translate/{id}"})
    public String translate(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            java.util.List<String> executed = this.leadershipAdminService.executeTranslationTasks(id);
            redirectAttributes.addFlashAttribute("success", "Translation tasks initialized."
                    + (executed.isEmpty() ? " (all languages already up to date)" : " Locales: " + String.join(", ", executed)));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start translation: " + e.getMessage());
        }
        return "redirect:/admin/leadership/";
    }

    @PostMapping(value={"/update/{id}"})
    public String update(@AuthenticationPrincipal AppUserDetails userDetails,
                         @PathVariable(value="id") Long id,
                         @RequestParam(value="fullName") String fullName,
                         @RequestParam(value="position", required=false) String position,
                         @RequestParam(value="organization", required=false) String organization,
                         @RequestParam(value="bio", required=false) String bio,
                         @RequestParam(value="displayOrder", required=false) Integer displayOrder,
                         @RequestParam(value="photo", required=false) MultipartFile photo,
                         RedirectAttributes redirectAttributes) {
        try {
            this.leadershipAdminService.update(id, fullName, position, organization, bio, displayOrder, photo, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Member updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update member: " + e.getMessage());
        }
        return "redirect:/admin/leadership/";
    }

    @GetMapping(value={"/translations/{id}"})
    public String translations(@PathVariable(value="id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("member", this.leadershipAdminService.getById(id));
            model.addAttribute("form", this.leadershipAdminService.getTranslationsForm(id));
            model.addAttribute("memberId", id);
            return "admin/leadership/translations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/leadership/";
        }
    }

    @PostMapping(value={"/translations/{id}"})
    public String saveTranslations(@PathVariable(value="id") Long id,
                                   @ModelAttribute(value="form") LeadershipMemberTranslationsForm form,
                                   RedirectAttributes redirectAttributes) {
        try {
            this.leadershipAdminService.saveTranslations(id, form);
            redirectAttributes.addFlashAttribute("success", "Translations saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save translations: " + e.getMessage());
        }
        return "redirect:/admin/leadership/translations/" + id;
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.leadershipAdminService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Member deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete member: " + e.getMessage());
        }
        return "redirect:/admin/leadership/";
    }
}
