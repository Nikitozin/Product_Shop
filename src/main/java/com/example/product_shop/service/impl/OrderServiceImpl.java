package com.example.product_shop.service.impl;

import com.example.product_shop.model.Product;
import com.example.product_shop.repository.OrderRepository;
import com.example.product_shop.service.OrderService;
import com.example.product_shop.service.ProductService;
import com.example.product_shop.dto.request.OrderRequest;
import com.example.product_shop.dto.request.OrderItemRequest;
import com.example.product_shop.model.Order;
import com.example.product_shop.model.OrderItem;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private ProductService productService;

    private static final int MINUTES_FOR_PAY = 10;

    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Order createOrder(OrderRequest orderRequest) {
        if (orderRequest != null && !orderRequest.getItems().isEmpty()) {
            Order order = new Order();
            order.setCreatedAt(LocalDateTime.now());
            order.setPaid(false);

            List<OrderItem> orderItems = new ArrayList<>();

            validateAndAddOrderItems(orderRequest, order, orderItems);

            if (!orderItems.isEmpty()) {
                order.setItems(orderItems);
                order = orderRepository.save(order);
                updateQuantityOfProducts(orderItems);
                return order;
            }
        }
        throw new IllegalArgumentException("Invalid order request.");
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found. Id:" + orderId));
        if (!order.isPaid()) {
            order.setPaid(true);
            order.setPaymentTime(LocalDateTime.now());
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("Order is already paid. Id:" + orderId);
        }
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> findByPaidFalseAndCreatedAtBeforeWithItems(LocalDateTime time) {
        return orderRepository.findByPaidFalseAndCreatedAtBeforeWithItems(time);
    }

    @Override
    public void removeOldUnpaidOrders() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Order> notPaidOrders = findByPaidFalseAndCreatedAtBeforeWithItems(currentTime.minusMinutes(MINUTES_FOR_PAY));
        if (notPaidOrders != null) {
            for (Order order : notPaidOrders) {
                if (order.getCreatedAt().isBefore(currentTime.minusMinutes(MINUTES_FOR_PAY))) {
                    returnProductsToStock(order);
                    deleteOrder(order.getId());
                }
            }
        }
    }

    @Override
    public void returnProductsToStock(Order order) {
        List<OrderItem> items = order.getItems();
        for (OrderItem item : items) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productService.addProduct(product);
        }
    }

    private void validateAndAddOrderItems(OrderRequest orderRequest, Order order, List<OrderItem> orderItems) {
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productService.getProductById(itemRequest.getProductId());
            if (product != null && product.getQuantity() >= itemRequest.getQuantity()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            } else {
                throw new IllegalArgumentException("Product not available or quantity is insufficient.");
            }
        }
    }

    private void updateQuantityOfProducts(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() - orderItem.getQuantity());
            productService.addProduct(product);
        }
    }
}
