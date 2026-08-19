package com.tiu.turk.user.admin.service;

import com.tiu.turk.user.common.entity.RoleEntity;
import com.tiu.turk.user.common.repository.RoleRepository;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Service
public class RoleAdminService {
    private final RoleRepository roleRepository;

    public List<RoleEntity> getAllRoles() {
        return this.roleRepository.findAll();
    }

    @Generated
    public RoleAdminService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}

