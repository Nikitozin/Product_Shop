package com.example.product_shop.filter;

import com.example.product_shop.model.Order;
import com.example.product_shop.model.OrderItem;
import com.example.product_shop.model.Product;
import com.example.product_shop.service.OrderService;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.product_shop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class OrderCleanupFilter extends OncePerRequestFilter {
    private OrderService orderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        orderService.removeOldUnpaidOrders();
        filterChain.doFilter(request, response);
    }
}
