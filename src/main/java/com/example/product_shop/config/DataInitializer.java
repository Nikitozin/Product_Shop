package com.example.product_shop.config;

import com.example.product_shop.model.Role;
import com.example.product_shop.model.RoleName;
import com.example.product_shop.model.User;
import com.example.product_shop.service.RoleService;
import com.example.product_shop.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class DataInitializer {
    private UserService userService;
    private RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void inject() {
        Role managerRole = new Role();
        managerRole.setRoleName(RoleName.MANAGER);
        roleService.addRole(managerRole);

        Role clientRole = new Role();
        clientRole.setRoleName(RoleName.CLIENT);
        roleService.addRole(clientRole);

        User manager = new User();
        manager.setUsername("firstManager");
        manager.setPassword("qwerty");
        manager.setRoles(Collections.singleton(managerRole));
        userService.registerUser(manager, RoleName.MANAGER);

        User client = new User();
        client.setUsername("firstClient");
        client.setPassword("qwerty");
        client.setRoles(Collections.singleton(clientRole));
        userService.registerUser(client, RoleName.CLIENT);
    }
}
