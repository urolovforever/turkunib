package com.tiu.turk.quota.admin.controller;

import com.tiu.turk.quota.admin.dto.QuotaGridForm;
import com.tiu.turk.quota.admin.dto.QuotaRowForm;
import com.tiu.turk.quota.admin.service.OrhunQuotaAdminService;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.user.common.security.AppUserDetails;
import java.util.List;
import lombok.Generated;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/orhun-quotas"})
public class OrhunQuotaAdminController {
    private final OrhunQuotaAdminService quotaService;

    @GetMapping(value={"/", "/index"})
    public String index(@RequestParam(value="roundId", required=false) Long roundId,
                        @AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        List<ScholarshipEntity> rounds = this.quotaService.getRounds();
        model.addAttribute("rounds", rounds);
        if (rounds.isEmpty()) {
            model.addAttribute("round", null);
            return "admin/quota/index";
        }
        ScholarshipEntity round = roundId != null ? this.quotaService.getRound(roundId) : rounds.get(0);
        if (round == null) round = rounds.get(0);
        model.addAttribute("round", round);

        boolean memberView = userDetails.isUniversityMember() && userDetails.getMemberId() != null;
        if (memberView) {
            model.addAttribute("row", this.quotaService.buildMemberRow(round.getId(), userDetails.getMemberId()));
            return "admin/quota/member-form";
        }
        model.addAttribute("gridRows", this.quotaService.buildGrid(round.getId()));
        return "admin/quota/index";
    }

    @PostMapping(value={"/save"})
    public String saveGrid(@ModelAttribute QuotaGridForm form,
                           @AuthenticationPrincipal AppUserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        if (userDetails.isUniversityMember()) {
            redirectAttributes.addFlashAttribute("error", "You can only edit your own university's quota.");
            return "redirect:/admin/orhun-quotas/?roundId=" + form.getScholarshipId();
        }
        this.quotaService.saveGrid(form.getScholarshipId(), form.getRows(), userDetails.getId());
        redirectAttributes.addFlashAttribute("success", "Quotas saved.");
        return "redirect:/admin/orhun-quotas/?roundId=" + form.getScholarshipId();
    }

    @PostMapping(value={"/save-member"})
    public String saveMember(@RequestParam(value="scholarshipId") Long scholarshipId,
                             @ModelAttribute QuotaRowForm row,
                             @AuthenticationPrincipal AppUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        // The member id always comes from the logged-in session, never from the form.
        if (!userDetails.isUniversityMember() || userDetails.getMemberId() == null) {
            redirectAttributes.addFlashAttribute("error", "Only university accounts can use this form.");
            return "redirect:/admin/orhun-quotas/?roundId=" + scholarshipId;
        }
        this.quotaService.saveForMember(scholarshipId, userDetails.getMemberId(), row, userDetails.getId());
        redirectAttributes.addFlashAttribute("success", "Your university's quota has been saved.");
        return "redirect:/admin/orhun-quotas/?roundId=" + scholarshipId;
    }

    @Generated
    public OrhunQuotaAdminController(OrhunQuotaAdminService quotaService) {
        this.quotaService = quotaService;
    }
}
