package com.tiu.turk.configuration;

import com.tiu.turk.authentication.web.captcha.CaptchaService;
import com.tiu.turk.authentication.web.security.LoginAttemptService;
import com.tiu.turk.authentication.web.security.LoginProtectionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CaptchaService captchaService;
    private final LoginAttemptService loginAttemptService;

    public SecurityConfig(CaptchaService captchaService, LoginAttemptService loginAttemptService) {
        this.captchaService = captchaService;
        this.loginAttemptService = loginAttemptService;
    }

    /**
     * Admin / member-university area. Scoped to /admin/** so it does not interfere
     * with the public site or the student login flow.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/admin/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/login").permitAll()
                        // Applications stay off-limits for CONTENT_ADMIN (content-only administrator).
                        .requestMatchers("/admin/application-process/**", "/admin/application/**").hasAnyRole("UNIVERSITY_MEMBER", "ADMIN")
                        .requestMatchers("/admin/orhun-quotas/**").hasAnyRole("UNIVERSITY_MEMBER", "ADMIN")
                        .requestMatchers("/admin/dashboard/**").hasAnyRole("UNIVERSITY_MEMBER", "ADMIN", "CONTENT_ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "CONTENT_ADMIN")
                        .anyRequest().authenticated()
                ).exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, denied) ->
                                response.sendRedirect(request.getContextPath() + "/admin/dashboard"))
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                ).formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    /**
     * Public site + student portal. The student dashboard (/{lang}/student-dashboard)
     * requires ROLE_STUDENT; everything else stays public. Students sign in at /login
     * and register at /register.
     *
     * The student form login is additionally protected by LoginProtectionFilter
     * (image CAPTCHA + per-email/IP lockout) before the credentials are checked.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/*/student-dashboard", "/*/application/**").hasRole("STUDENT")
                        .anyRequest().permitAll()
                ).exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, denied) ->
                                response.sendRedirect(request.getContextPath() + "/login?denied"))
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                ).addFilterBefore(new LoginProtectionFilter(this.captchaService, this.loginAttemptService),
                        UsernamePasswordAuthenticationFilter.class
                ).formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(roleAwareSuccessHandler())
                        .failureHandler(studentLoginFailureHandler())
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    /**
     * After a successful sign-in on the public /login page, send the user to the right place
     * based on their role: students to their dashboard, admins/member universities to the admin area.
     * This prevents a 403 when a non-student signs in through the student login form.
     */
    private AuthenticationSuccessHandler roleAwareSuccessHandler() {
        return (request, response, authentication) -> {
            this.loginAttemptService.recordSuccess(
                    request.getParameter("username"), LoginAttemptService.clientIp(request));
            boolean isStudent = authentication.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_STUDENT".equals(a.getAuthority()));
            String target = isStudent ? "/en/student-dashboard" : "/admin/dashboard";
            response.sendRedirect(request.getContextPath() + target);
        };
    }

    /**
     * Distinguishes a not-yet-activated account from bad credentials, and feeds the
     * brute-force counter only on genuine credential failures.
     */
    private AuthenticationFailureHandler studentLoginFailureHandler() {
        return (request, response, exception) -> {
            String target = "/login?error";
            if (exception instanceof DisabledException) {
                target = "/login?disabled";
            } else {
                this.loginAttemptService.recordFailure(
                        request.getParameter("username"), LoginAttemptService.clientIp(request));
            }
            response.sendRedirect(request.getContextPath() + target);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
