package com.tiu.turk.applicationprocess.admin.controller;

import com.tiu.turk.applicationprocess.admin.dto.CreateApplicationDto;
import com.tiu.turk.applicationprocess.admin.mapper.ApplicationProcessMapper;
import com.tiu.turk.applicationprocess.admin.service.ApplicationProcessService;
import com.tiu.turk.applicationprocess.common.ApplicationProcessStatus;
import com.tiu.turk.applicationprocess.common.entity.ApplicationProcessEntity;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.user.common.security.AppUserDetails;
import java.util.List;
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
@RequestMapping(value={"/admin/application-process"})
@RequiredArgsConstructor
public class ApplicationProcessController {
    private final ApplicationProcessService applicationProcessService;
    private final MemberRepository memberRepository;
    private final ApplicationProcessMapper processMapper;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getIndex(@AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        List<ApplicationProcessEntity> applications;
        if (userDetails.isUniversityMember() && userDetails.getMemberId() != null) {
            applications = this.applicationProcessService.getApplicationsByMemberId(userDetails.getMemberId());
        } else {
            applications = this.applicationProcessService.getAllApplications();
        }
        model.addAttribute("applications", this.processMapper.toDtoList(applications));
        return "admin/application-process/index";
    }

    @GetMapping(value={"/show/{id}"})
    public String show(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ApplicationProcessEntity application = this.applicationProcessService.getById(id);
            model.addAttribute("applicationEntity", this.processMapper.toDto(application));
            return "admin/application-process/show";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Application not found: " + e.getMessage());
            return "redirect:/admin/application-process/";
        }
    }

    @GetMapping(value={"/create"})
    public String getCreateForm(Model model) {
        model.addAttribute("universities", this.memberRepository.findAll());
        return "admin/application-process/create";
    }

    @PostMapping(value={"/create"})
    public String createApplication(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @ModelAttribute(value="applicationRequest") CreateApplicationDto applicationRequest, RedirectAttributes redirectAttributes) {
        try {
            this.applicationProcessService.createApplication(applicationRequest, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Application submitted successfully!");
            return "redirect:/admin/application-process/";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit application: " + e.getMessage());
            return "redirect:/admin/application-process/create";
        }
    }

    @PostMapping(value={"/update-status/{id}"})
    public String updateStatus(@PathVariable Long id, @RequestParam(value="status") ApplicationProcessStatus status, RedirectAttributes redirectAttributes) {
        try {
            this.applicationProcessService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Status updated successfully!");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update status: " + e.getMessage());
        }
        return "redirect:/admin/application-process/show/" + id;
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.applicationProcessService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Application deleted successfully!");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete application: " + e.getMessage());
        }
        return "redirect:/admin/application-process/";
    }
}
