package com.example.product_shop.service;

import com.example.product_shop.dto.request.OrderRequest;
import com.example.product_shop.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);

    List<Order> getAllOrders();

    void payOrder(Long orderId);

    void deleteOrder(Long orderId);

    List<Order> findByPaidFalseAndCreatedAtBeforeWithItems(LocalDateTime time);

    void removeOldUnpaidOrders();

    void returnProductsToStock(Order order);
}
