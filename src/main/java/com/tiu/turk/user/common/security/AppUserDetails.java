package com.tiu.turk.user.common.security;

import com.tiu.turk.user.common.entity.UserEntity;
import java.util.Collection;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails
implements UserDetails {
    private final UserEntity user;

    public AppUserDetails(UserEntity user) {
        this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public String getUsername() {
        return Optional.ofNullable(this.user.getEmail()).map(String::trim).map(String::toLowerCase).orElse("");
    }

    public boolean isEnabled() {
        return this.user.getEnabled();
    }

    public String getFullName() {
        return this.user.getFirstName() + " " + this.user.getLastName();
    }

    public Long getId() {
        return this.user.getId();
    }

    public java.time.LocalDateTime getCreatedAt() {
        return this.user.getCreatedAt();
    }

    public Long getMemberId() {
        return this.user.getMember() != null ? this.user.getMember().getId() : null;
    }

    public String getMemberName() {
        return this.user.getMember() != null ? this.user.getMember().getName() : null;
    }

    public boolean isUniversityMember() {
        return this.user.getRoles().stream()
                .anyMatch(r -> "ROLE_UNIVERSITY_MEMBER".equals(r.getName()));
    }
}
