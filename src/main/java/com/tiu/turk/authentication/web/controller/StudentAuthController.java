package com.tiu.turk.authentication.web.controller;

import com.tiu.turk.authentication.web.captcha.CaptchaService;
import com.tiu.turk.authentication.web.dto.StudentRegisterDto;
import com.tiu.turk.authentication.web.service.StudentAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentAuthController {
    private final StudentAuthService studentAuthService;
    private final CaptchaService captchaService;
    private final MessageSource messages;

    public StudentAuthController(StudentAuthService studentAuthService, CaptchaService captchaService,
                                 MessageSource messages) {
        this.studentAuthService = studentAuthService;
        this.captchaService = captchaService;
        this.messages = messages;
    }

    @GetMapping(value = {"/login"})
    public String showLoginPage() {
        return "web/auth/login";
    }

    @GetMapping(value = {"/register"})
    public String showRegisterPage(Model model) {
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new StudentRegisterDto());
        }
        return "web/auth/register";
    }

    @PostMapping(value = {"/register"})
    public String register(@ModelAttribute("registerForm") StudentRegisterDto registerForm,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        if (!this.captchaService.verify(request.getSession(false), request.getParameter("captchaAnswer"))) {
            return registerError(registerForm, redirectAttributes, "auth.captcha.wrong");
        }
        try {
            this.studentAuthService.register(registerForm, LocaleContextHolder.getLocale());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return registerError(registerForm, redirectAttributes, ex.getMessage());
        }
        if (this.studentAuthService.isActivationEnabled()) {
            return "redirect:/activation-sent";
        }
        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/login?registered";
    }

    private String registerError(StudentRegisterDto registerForm, RedirectAttributes redirectAttributes, String messageKey) {
        registerForm.setPassword(null);
        registerForm.setConfirmPassword(null);
        redirectAttributes.addFlashAttribute("error",
                this.messages.getMessage(messageKey, null, messageKey, LocaleContextHolder.getLocale()));
        redirectAttributes.addFlashAttribute("registerForm", registerForm);
        return "redirect:/register";
    }
}
