package com.example.product_shop.service.impl;

import com.example.product_shop.model.Role;
import com.example.product_shop.model.RoleName;
import com.example.product_shop.model.User;
import com.example.product_shop.repository.RoleRepository;
import com.example.product_shop.repository.UserRepository;
import com.example.product_shop.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.springframework.security.core.userdetails.User.UserBuilder;
import static org.springframework.security.core.userdetails.User.withUsername;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserBuilder builder = withUsername(username)
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(r -> r.getRoleName().name())
                        .toArray(String[]::new));
        return builder.build();
    }

    @Override
    public void registerUser(User user, RoleName roleName) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            throw new RuntimeException("Role not found: " + roleName.name());
        }
        user.setRoles(Collections.singleton(role));
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }
}