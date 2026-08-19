package com.tiu.turk.authentication.web.service;

import com.tiu.turk.authentication.web.dto.StudentRegisterDto;
import com.tiu.turk.user.common.entity.RoleEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import com.tiu.turk.user.common.repository.RoleRepository;
import com.tiu.turk.user.common.repository.UserRepository;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentAuthService {
    public static final String STUDENT_ROLE = "ROLE_STUDENT";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;

    /**
     * When true, new accounts start disabled and must be activated through the
     * e-mailed link. Keep false until the SMTP sender (Mailjet) is verified in
     * production, otherwise new students could never sign in.
     */
    @Value("${app.activation.enabled:false}")
    private boolean activationEnabled;

    public StudentAuthService(UserRepository userRepository, RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder, AuthTokenService authTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authTokenService = authTokenService;
    }

    public boolean isActivationEnabled() {
        return this.activationEnabled;
    }

    /** Validation failures throw IllegalArgumentException whose message is an i18n key. */
    @Transactional
    public UserEntity register(StudentRegisterDto dto, Locale locale) {
        String email = normalizeEmail(dto.getEmail());

        if (email.isEmpty() || dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new IllegalArgumentException("auth.register.error.required");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("auth.register.error.password");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("auth.register.error.mismatch");
        }
        if (this.userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException("auth.register.error.exists");
        }

        RoleEntity studentRole = this.roleRepository.findByName(STUDENT_ROLE)
                .orElseThrow(() -> new IllegalStateException("auth.register.error.norole"));

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName(dto.getFirstName().trim());
        user.setLastName(dto.getLastName() != null ? dto.getLastName().trim() : null);
        user.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(studentRole);
        user.setRoles(roles);
        user.setEnabled(!this.activationEnabled);

        UserEntity saved = this.userRepository.save(user);
        if (this.activationEnabled) {
            this.authTokenService.sendActivation(saved, locale);
        }
        return saved;
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
