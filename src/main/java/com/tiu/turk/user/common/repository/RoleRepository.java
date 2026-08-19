package com.tiu.turk.user.common.repository;

import com.tiu.turk.user.common.entity.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository
extends JpaRepository<RoleEntity, Long> {
    public Optional<RoleEntity> findByName(String name);
}

