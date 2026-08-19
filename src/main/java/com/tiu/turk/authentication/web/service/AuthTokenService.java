package com.tiu.turk.authentication.web.service;

import com.tiu.turk.authentication.web.token.TokenPurpose;
import com.tiu.turk.authentication.web.token.UserTokenEntity;
import com.tiu.turk.authentication.web.token.UserTokenRepository;
import com.tiu.turk.user.common.entity.UserEntity;
import com.tiu.turk.user.common.repository.UserRepository;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Issues and redeems the one-time e-mailed tokens used by the password-reset
 * and account-activation flows. Raw tokens never touch the database — only
 * their SHA-256 hash is stored.
 */
@Service
public class AuthTokenService {
    public static final Duration RESET_VALIDITY = Duration.ofHours(1);
    public static final Duration ACTIVATION_VALIDITY = Duration.ofHours(24);

    private final UserTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final SecureRandom random = new SecureRandom();

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public AuthTokenService(UserTokenRepository tokenRepository, UserRepository userRepository,
                            PasswordEncoder passwordEncoder, MailService mailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    /* ---------- password reset ---------- */

    /** Silent when the e-mail is unknown, so the form never reveals whether an account exists. */
    @Transactional
    public void requestPasswordReset(String email, Locale locale) {
        Optional<UserEntity> user = findUser(email);
        if (user.isEmpty()) {
            return;
        }
        String rawToken = issueToken(user.get(), TokenPurpose.PASSWORD_RESET, RESET_VALIDITY);
        String link = this.baseUrl + "/reset-password?token=" + rawToken;
        this.mailService.sendPasswordReset(user.get(), link, locale);
    }

    /** Non-consuming validity check, used to decide whether to show the reset form. */
    @Transactional(readOnly = true)
    public boolean isResetTokenValid(String rawToken) {
        return findUsable(rawToken, TokenPurpose.PASSWORD_RESET).isPresent();
    }

    /**
     * Redeems the token and stores the new password.
     * Throws IllegalArgumentException with a message key on validation errors,
     * IllegalStateException when the token is invalid/expired/used.
     */
    @Transactional
    public void resetPassword(String rawToken, String password, String confirmPassword) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("auth.reset.tooshort");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("auth.reset.mismatch");
        }
        UserTokenEntity token = findUsable(rawToken, TokenPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new IllegalStateException("invalid token"));
        token.setUsedAt(LocalDateTime.now());
        UserEntity user = token.getUser();
        user.setPassword(this.passwordEncoder.encode(password));
        this.userRepository.save(user);
        this.tokenRepository.save(token);
    }

    /* ---------- account activation ---------- */

    @Transactional
    public void sendActivation(UserEntity user, Locale locale) {
        String rawToken = issueToken(user, TokenPurpose.ACTIVATION, ACTIVATION_VALIDITY);
        String link = this.baseUrl + "/activate?token=" + rawToken;
        this.mailService.sendActivation(user, link, locale);
    }

    /** Returns true when the account was activated (or was already active via this token's user). */
    @Transactional
    public boolean activate(String rawToken) {
        Optional<UserTokenEntity> token = findUsable(rawToken, TokenPurpose.ACTIVATION);
        if (token.isEmpty()) {
            return false;
        }
        token.get().setUsedAt(LocalDateTime.now());
        UserEntity user = token.get().getUser();
        user.setEnabled(true);
        this.userRepository.save(user);
        this.tokenRepository.save(token.get());
        return true;
    }

    /** Silent when the e-mail is unknown or the account is already active. */
    @Transactional
    public void resendActivation(String email, Locale locale) {
        Optional<UserEntity> user = findUser(email);
        if (user.isEmpty() || Boolean.TRUE.equals(user.get().getEnabled())) {
            return;
        }
        sendActivation(user.get(), locale);
    }

    /* ---------- helpers ---------- */

    private Optional<UserEntity> findUser(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return this.userRepository.findByEmailIgnoreCase(email.trim().toLowerCase());
    }

    private String issueToken(UserEntity user, TokenPurpose purpose, Duration validity) {
        byte[] bytes = new byte[32];
        this.random.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        UserTokenEntity token = new UserTokenEntity();
        token.setUser(user);
        token.setTokenHash(DigestUtils.sha256Hex(rawToken));
        token.setPurpose(purpose);
        token.setExpiresAt(LocalDateTime.now().plus(validity));
        this.tokenRepository.save(token);
        return rawToken;
    }

    private Optional<UserTokenEntity> findUsable(String rawToken, TokenPurpose purpose) {
        if (rawToken == null || rawToken.isBlank()) {
            return Optional.empty();
        }
        return this.tokenRepository.findByTokenHashAndPurpose(DigestUtils.sha256Hex(rawToken.trim()), purpose)
                .filter(UserTokenEntity::isUsable);
    }
}
