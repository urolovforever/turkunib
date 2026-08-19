package com.tiu.turk.application.admin.controller;

import com.tiu.turk.application.admin.service.ApplicationService;
import com.tiu.turk.application.admin.service.ApplicationStatsService;
import com.tiu.turk.application.common.ApplicantType;
import com.tiu.turk.application.common.ApplicationDecision;
import com.tiu.turk.application.common.ApplicationStatus;
import com.tiu.turk.application.common.Semester;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
import com.tiu.turk.user.common.security.AppUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/application"})
@RequiredArgsConstructor
public class ApplicationAdminController {
    private final ApplicationService applicationService;
    private final ApplicationStatsService applicationStatsService;
    private final MemberRepository memberRepository;
    private final ScholarshipRepository scholarshipRepository;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getAllApplications(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(required=false) String q, Model model, Pageable pageable) {
        boolean memberView = userDetails.isUniversityMember() && userDetails.getMemberId() != null;
        model.addAttribute("memberView", memberView);
        if (memberView) {
            model.addAttribute("outgoing", this.applicationService.getOutgoingForMember(userDetails.getMemberId(), userDetails.getMemberName()));
            model.addAttribute("incoming", this.applicationService.getIncomingForMember(userDetails.getMemberId()));
        } else {
            Page<ApplicationEntity> page = this.applicationService.getAllApplications(pageable.getPageNumber(), pageable.getPageSize());
            model.addAttribute("page", page);
        }
        model.addAttribute("q", q);
        return "admin/application/index";
    }

    @GetMapping(value={"/statistics"})
    public String statistics(@AuthenticationPrincipal AppUserDetails userDetails,
                             @RequestParam(required=false) Long round,
                             @RequestParam(required=false) Semester semester,
                             @RequestParam(required=false) ApplicantType type,
                             Model model) {
        boolean memberView = userDetails.isUniversityMember() && userDetails.getMemberId() != null;
        model.addAttribute("memberView", memberView);
        model.addAttribute("rounds", this.scholarshipRepository.findAllByOrderByDisplayOrderAscIdAsc());
        model.addAttribute("semesters", Semester.values());
        model.addAttribute("types", ApplicantType.values());
        model.addAttribute("round", round);
        model.addAttribute("semester", semester);
        model.addAttribute("type", type);
        if (memberView) {
            List<ApplicationEntity> outgoing = this.applicationStatsService.filter(
                    this.applicationService.getOutgoingForMember(userDetails.getMemberId(), userDetails.getMemberName()),
                    round, semester, type);
            List<ApplicationEntity> incoming = this.applicationStatsService.filter(
                    this.applicationService.getIncomingForMember(userDetails.getMemberId()),
                    round, semester, type);
            model.addAttribute("outgoingStats", this.applicationStatsService.build(outgoing));
            model.addAttribute("incomingStats", this.applicationStatsService.build(incoming));
        } else {
            List<ApplicationEntity> apps = this.applicationStatsService.filter(
                    this.applicationService.getAllForStatistics(), round, semester, type);
            model.addAttribute("stats", this.applicationStatsService.build(apps));
        }
        return "admin/application/statistics";
    }

    @GetMapping(value={"/show/{applicationId}"})
    public String showApplication(@PathVariable(value="applicationId") Long applicationId,
                                  @AuthenticationPrincipal AppUserDetails userDetails,
                                  Model model, RedirectAttributes redirectAttributes) {
        ApplicationEntity entity = this.applicationService.getApplicationById(applicationId);
        if (!this.applicationService.canView(entity, userDetails)) {
            redirectAttributes.addFlashAttribute("error", "You do not have access to this application.");
            return "redirect:/admin/application/";
        }
        model.addAttribute("appEntity", entity);
        model.addAttribute("members", this.memberRepository.findAll());
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("canHomeReview", this.applicationService.canHomeReview(entity, userDetails));
        model.addAttribute("canHostDecide", this.applicationService.canHostDecide(entity, userDetails));
        model.addAttribute("isFullAdmin", !userDetails.isUniversityMember());
        return "admin/application/show";
    }

    @PostMapping(value={"/decide/{applicationId}"})
    public String decide(@PathVariable(value="applicationId") Long applicationId,
                         @AuthenticationPrincipal AppUserDetails userDetails,
                         @RequestParam(value="decision") ApplicationDecision decision,
                         @RequestParam(value="acceptanceFile", required=false) MultipartFile acceptanceFile,
                         RedirectAttributes redirectAttributes) {
        try {
            this.applicationService.decide(applicationId, decision, acceptanceFile, userDetails);
            redirectAttributes.addFlashAttribute("success", switch (decision) {
                case HOME_APPROVE -> "Application approved and forwarded to the host university.";
                case HOME_REJECT -> "Application rejected.";
                case ACCEPT -> "Application accepted.";
                case REJECT -> "Application rejected.";
            });
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update application: " + e.getMessage());
        }
        return "redirect:/admin/application/show/" + applicationId;
    }

    @PostMapping(value={"/manage/{applicationId}"})
    public String manageApplication(@PathVariable(value="applicationId") Long applicationId,
                                    @AuthenticationPrincipal AppUserDetails userDetails,
                                    @RequestParam(value="status", required=false) ApplicationStatus status,
                                    @RequestParam(value="matchedUniversityId", required=false) Long matchedUniversityId,
                                    @RequestParam(value="file", required=false) MultipartFile file,
                                    RedirectAttributes redirectAttributes) {
        if (userDetails.isUniversityMember()) {
            redirectAttributes.addFlashAttribute("error", "Only site administrators can use the management form.");
            return "redirect:/admin/application/show/" + applicationId;
        }
        try {
            this.applicationService.manageApplication(applicationId, status, matchedUniversityId, file, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Application updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update application: " + e.getMessage());
        }
        return "redirect:/admin/application/show/" + applicationId;
    }
}
