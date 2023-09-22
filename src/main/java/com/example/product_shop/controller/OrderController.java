package com.example.product_shop.controller;

import com.example.product_shop.dto.request.OrderRequest;
import com.example.product_shop.model.Order;
import com.example.product_shop.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/{orderId}/pay")
    public void payOrder(@PathVariable Long orderId) {
        orderService.payOrder(orderId);
    }
}