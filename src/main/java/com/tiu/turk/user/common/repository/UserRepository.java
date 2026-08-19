package com.tiu.turk.user.common.repository;

import com.tiu.turk.user.common.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByEmailIgnoreCase(String var1);
}

