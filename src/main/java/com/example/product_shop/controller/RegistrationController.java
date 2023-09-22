package com.example.product_shop.controller;

import com.example.product_shop.model.Role;
import com.example.product_shop.model.RoleName;
import com.example.product_shop.model.User;
import com.example.product_shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.stream.Collectors;

@Controller
public class RegistrationController {
    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerUser(user, RoleName.CLIENT);
        return "redirect:/login";
    }
}