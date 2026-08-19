package com.tiu.turk.user.admin.service;

import com.tiu.turk.user.common.entity.UserEntity;
import com.tiu.turk.user.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final List<String> PROHIBITED_DELETE_USER_NAMES = List.of("admin@gmail.com");

    public Page<UserEntity> getAllUsers(int page, int size) {
        return this.userRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public UserEntity getUserById(Long id) {
        return (UserEntity)this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public UserEntity saveUser(UserEntity newUser) {
        this.checkPassword(newUser.getPassword());
        this.checkEmail(newUser.getEmail());
        newUser.setPassword(this.passwordEncoder.encode((CharSequence)newUser.getPassword()));
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setCreatedAt(LocalDateTime.now());
        return (UserEntity)this.userRepository.save(newUser);
    }

    public UserEntity updateUser(Long id, UserEntity updatedUser) {
        this.checkEmail(updatedUser.getEmail());
        UserEntity existingUser = this.getUserById(id);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            this.checkPassword(updatedUser.getPassword());
            existingUser.setPassword(this.passwordEncoder.encode((CharSequence)updatedUser.getPassword()));
        }
        existingUser.setPassword(this.passwordEncoder.encode((CharSequence)updatedUser.getPassword()));
        existingUser.setRoles(updatedUser.getRoles());
        existingUser.setEnabled(updatedUser.getEnabled());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return (UserEntity)this.userRepository.save(existingUser);
    }

    public void deleteUserById(Long id) {
        UserEntity user = this.getUserById(id);
        if (PROHIBITED_DELETE_USER_NAMES.contains(user.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Cannot delete protected user: " + user.getEmail());
        }
        this.userRepository.deleteById(id);
    }

    private void checkPassword(String password) throws RuntimeException {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    private void checkEmail(String email) throws RuntimeException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        String regexPattern = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
        if (!Pattern.compile("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$").matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    @Generated
    public UserAdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
}

