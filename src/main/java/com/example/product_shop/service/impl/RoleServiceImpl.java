package com.example.product_shop.service.impl;

import com.example.product_shop.model.Role;
import com.example.product_shop.repository.RoleRepository;
import com.example.product_shop.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void addRole(Role role) {
        roleRepository.save(role);
    }
}
