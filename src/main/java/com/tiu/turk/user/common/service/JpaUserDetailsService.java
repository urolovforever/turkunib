package com.tiu.turk.user.common.service;

import com.tiu.turk.user.common.repository.UserRepository;
import com.tiu.turk.user.common.security.AppUserDetails;
import lombok.Generated;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService
implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails)this.userRepository.findByEmailIgnoreCase(username).map(AppUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Generated
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

