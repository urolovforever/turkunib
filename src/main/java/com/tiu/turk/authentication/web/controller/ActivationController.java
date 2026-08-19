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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ActivationController {
    private static final int MAX_RESEND_REQUESTS_PER_HOUR = 5;

    private final AuthTokenService authTokenService;
    private final CaptchaService captchaService;
    private final RateLimitService rateLimitService;
    private final MessageSource messages;

    public ActivationController(AuthTokenService authTokenService, CaptchaService captchaService,
                                RateLimitService rateLimitService, MessageSource messages) {
        this.authTokenService = authTokenService;
        this.captchaService = captchaService;
        this.rateLimitService = rateLimitService;
        this.messages = messages;
    }

    @GetMapping("/activate")
    public String activate(@RequestParam(value = "token", required = false) String token) {
        if (token != null && this.authTokenService.activate(token)) {
            return "redirect:/login?activated";
        }
        return "redirect:/resend-activation?invalid";
    }

    @GetMapping("/activation-sent")
    public String activationSent() {
        return "web/auth/activation_sent";
    }

    @GetMapping("/resend-activation")
    public String showResendForm() {
        return "web/auth/resend_activation";
    }

    @PostMapping("/resend-activation")
    public String resend(@RequestParam("email") String email,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {
        if (!this.captchaService.verify(request.getSession(false), request.getParameter("captchaAnswer"))) {
            redirectAttributes.addFlashAttribute("error", localized("auth.captcha.wrong"));
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/resend-activation";
        }
        String ip = LoginAttemptService.clientIp(request);
        if (!this.rateLimitService.tryAcquire("resend|" + ip, MAX_RESEND_REQUESTS_PER_HOUR, Duration.ofHours(1))) {
            redirectAttributes.addFlashAttribute("error", localized("auth.forgot.ratelimited"));
            return "redirect:/resend-activation";
        }
        this.authTokenService.resendActivation(email, LocaleContextHolder.getLocale());
        return "redirect:/resend-activation?sent";
    }

    private String localized(String key) {
        return this.messages.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }
}
