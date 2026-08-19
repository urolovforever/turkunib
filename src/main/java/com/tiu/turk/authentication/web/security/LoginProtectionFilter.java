package com.tiu.turk.authentication.web.security;

import com.tiu.turk.authentication.web.captcha.CaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Runs in front of the student form-login processing endpoint and rejects the
 * attempt before Spring Security touches the password when either
 * (a) the email+IP pair is currently locked out, or (b) the CAPTCHA answer is wrong.
 *
 * Registered manually in SecurityConfig (not a @Component) so it only takes part
 * in the student security chain and is not double-registered as a servlet filter.
 */
public class LoginProtectionFilter extends OncePerRequestFilter {
    public static final String CAPTCHA_PARAMETER = "captchaAnswer";
    private static final String LOGIN_PROCESSING_URL = "/perform-login";

    private final CaptchaService captchaService;
    private final LoginAttemptService loginAttemptService;

    public LoginProtectionFilter(CaptchaService captchaService, LoginAttemptService loginAttemptService) {
        this.captchaService = captchaService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!isLoginAttempt(request)) {
            chain.doFilter(request, response);
            return;
        }

        String username = LoginAttemptService.normalizeUsername(request.getParameter("username"));
        String ip = LoginAttemptService.clientIp(request);

        if (this.loginAttemptService.isLocked(username, ip)) {
            response.sendRedirect(request.getContextPath() + "/login?locked");
            return;
        }
        if (!this.captchaService.verify(request.getSession(false), request.getParameter(CAPTCHA_PARAMETER))) {
            response.sendRedirect(request.getContextPath() + "/login?captcha");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isLoginAttempt(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                && LOGIN_PROCESSING_URL.equals(request.getServletPath());
    }
}
