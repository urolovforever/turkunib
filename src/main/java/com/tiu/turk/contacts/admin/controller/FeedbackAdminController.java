package com.tiu.turk.contacts.admin.controller;

import com.tiu.turk.contacts.admin.dto.FeedbackDto;
import com.tiu.turk.contacts.admin.mapper.FeedbackAdminMapper;
import com.tiu.turk.contacts.admin.service.FeedbackAdminService;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/feedback"})
public class FeedbackAdminController {
    private final FeedbackAdminService feedbackAdminService;
    private final FeedbackAdminMapper feedbackAdminMapper;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getAllFeedbacks(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page page = this.feedbackAdminService.getAllFeedbacks(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.feedbackAdminMapper.toDto(arg_0));
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "admin/feedback/index";
    }

    @GetMapping(value={"/show/{feedbackId}"})
    public String showFeedback(@PathVariable(value="feedbackId") Long feedbackId, Model model) {
        FeedbackDto feedback = this.feedbackAdminMapper.toDto(this.feedbackAdminService.getFeedbackById(feedbackId));
        model.addAttribute("feedback", feedback);
        return "admin/feedback/show";
    }

    @PostMapping(value={"/delete/{feedbackId}"})
    public String deleteFeedback(@PathVariable(value="feedbackId") Long feedbackId, RedirectAttributes redirectAttributes) {
        try {
            this.feedbackAdminService.deleteFeedbackById(feedbackId);
            redirectAttributes.addFlashAttribute("success", "Feedback deleted successfully.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", ("Error deleting feedback: " + e.getMessage()));
        }
        return "redirect:/admin/feedback/index";
    }

    @Generated
    public FeedbackAdminController(FeedbackAdminService feedbackAdminService, FeedbackAdminMapper feedbackAdminMapper) {
        this.feedbackAdminService = feedbackAdminService;
        this.feedbackAdminMapper = feedbackAdminMapper;
    }
}

