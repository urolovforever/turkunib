package com.tiu.turk.newsletter.web.controller;

import com.tiu.turk.newsletter.common.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/newsletter"})
@RequiredArgsConstructor
public class NewsletterWebController {
    private final NewsletterService newsletterService;

    @PostMapping(value={"/subscribe"})
    public String subscribe(@PathVariable(value="lang") String lang,
                            @RequestParam(value="email") String email,
                            @RequestHeader(value="Referer", required=false) String referer,
                            RedirectAttributes redirectAttributes) {
        try {
            this.newsletterService.subscribe(email);
            redirectAttributes.addFlashAttribute("newsletterSuccess", "Thank you! You have subscribed to the TURKUNIB newsletter.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("newsletterError", e.getMessage());
        }
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/" + lang;
    }
}
