package com.tiu.turk.authentication.web.controller;

import com.tiu.turk.authentication.web.captcha.CaptchaService;
import com.tiu.turk.authentication.web.security.LoginAttemptService;
import com.tiu.turk.authentication.web.security.RateLimitService;
import com.tiu.turk.authentication.web.service.AuthTokenService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {
    private static final int MAX_RESET_REQUESTS_PER_HOUR = 5;

    private final AuthTokenService authTokenService;
    private final CaptchaService captchaService;
    private final RateLimitService rateLimitService;
    private final MessageSource messages;

    public PasswordResetController(AuthTokenService authTokenService, CaptchaService captchaService,
                                   RateLimitService rateLimitService, MessageSource messages) {
        this.authTokenService = authTokenService;
        this.captchaService = captchaService;
        this.rateLimitService = rateLimitService;
        this.messages = messages;
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "web/auth/forgot_password";
    }

    @PostMapping("/forgot-password")
    public String requestReset(@RequestParam("email") String email,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        if (!this.captchaService.verify(request.getSession(false), request.getParameter("captchaAnswer"))) {
            redirectAttributes.addFlashAttribute("error", localized("auth.captcha.wrong"));
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/forgot-password";
        }
        String ip = LoginAttemptService.clientIp(request);
        if (!this.rateLimitService.tryAcquire("reset|" + ip, MAX_RESET_REQUESTS_PER_HOUR, Duration.ofHours(1))) {
            redirectAttributes.addFlashAttribute("error", localized("auth.forgot.ratelimited"));
            return "redirect:/forgot-password";
        }
        this.authTokenService.requestPasswordReset(email, LocaleContextHolder.getLocale());
        return "redirect:/forgot-password?sent";
    }

    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || !this.authTokenService.isResetTokenValid(token)) {
            model.addAttribute("invalidToken", true);
        } else {
            model.addAttribute("token", token);
        }
        return "web/auth/reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            this.authTokenService.resetPassword(token, password, confirmPassword);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", localized(ex.getMessage()));
            return "redirect:/reset-password?token=" + token;
        } catch (IllegalStateException ex) {
            return "redirect:/reset-password";
        }
        return "redirect:/login?reset";
    }

    private String localized(String key) {
        return this.messages.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }
}
