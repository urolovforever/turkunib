package com.tiu.turk.newsletter.admin.controller;

import com.tiu.turk.newsletter.common.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/admin/newsletter"})
@RequiredArgsConstructor
public class NewsletterAdminController {
    private final NewsletterService newsletterService;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("subscribers", this.newsletterService.getAll());
        model.addAttribute("total", this.newsletterService.count());
        return "admin/newsletter/index";
    }
}
