package com.example.product_shop.service;

import com.example.product_shop.model.RoleName;
import com.example.product_shop.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);

    void registerUser(User user, RoleName roleName);
}
