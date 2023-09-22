package com.example.product_shop.config;

import com.example.product_shop.filter.OrderCleanupFilter;
import com.example.product_shop.model.Role;
import com.example.product_shop.model.RoleName;
import com.example.product_shop.model.User;
import com.example.product_shop.service.OrderService;
import com.example.product_shop.service.ProductService;
import com.example.product_shop.service.RoleService;
import com.example.product_shop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/products").hasRole("MANAGER")
                .antMatchers(HttpMethod.GET, "/products").hasAnyRole("MANAGER", "CLIENT")
                .antMatchers(HttpMethod.POST, "/orders").hasRole("CLIENT")
                .antMatchers(HttpMethod.GET, "/orders").hasAnyRole("MANAGER", "CLIENT")
                .antMatchers("/registration").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();

        http
                .addFilterBefore(new OrderCleanupFilter(orderService), BasicAuthenticationFilter.class);

    }
}
