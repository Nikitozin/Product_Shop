package com.example.product_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication(scanBasePackages = "com.example.product_shop")
public class ProductShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductShopApplication.class, args);
    }

}
