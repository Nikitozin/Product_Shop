package com.example.product_shop.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import com.example.product_shop.dto.request.OrderItemRequest;
import com.example.product_shop.dto.request.OrderRequest;
import com.example.product_shop.model.Order;
import com.example.product_shop.model.OrderItem;
import com.example.product_shop.model.Product;
import com.example.product_shop.repository.OrderRepository;
import com.example.product_shop.service.OrderService;
import com.example.product_shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderServiceImpl(orderRepository, productService);
    }

    @Test
    public void testRemoveOldUnpaidOrders() {
        LocalDateTime timeForOldOrder = LocalDateTime.now().minusMinutes(11);
        List<Order> notPaidOrders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setCreatedAt(timeForOldOrder);
        OrderItem item1 = new OrderItem();
        item1.setProduct(new Product());
        item1.setQuantity(2);
        order1.setItems(Collections.singletonList(item1));
        notPaidOrders.add(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setCreatedAt(LocalDateTime.now());
        OrderItem item2 = new OrderItem();
        item2.setProduct(new Product());
        item2.setQuantity(2);
        order2.setItems(Collections.singletonList(item2));
        notPaidOrders.add(order2);

        when(orderRepository.findByPaidFalseAndCreatedAtBeforeWithItems(any())).thenReturn(notPaidOrders);

        orderService.removeOldUnpaidOrders();

        verify(orderRepository, times(1)).deleteById(1L); // Verify that order1 is deleted once
        verify(orderRepository, never()).deleteById(2L); // Verify that order2 is deleted once
        verify(productService, times(1)).addProduct(any());
    }

    @Test
    public void testReturnProductsToStock() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        Product product = new Product();
        product.setQuantity(10);
        item.setProduct(product);
        item.setQuantity(5);
        order.setItems(Collections.singletonList(item));

        orderService.returnProductsToStock(order);

        verify(productService, times(1)).addProduct(product);

        assertEquals(15, product.getQuantity());
    }

    @Test
    public void testCreateOrder_ValidOrderRequest_ReturnsOrder() {
        OrderRequest orderRequest = new OrderRequest();
        List<OrderItemRequest> itemRequests = new ArrayList<>();
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(1l);
        orderItemRequest.setQuantity(3);
        itemRequests.add(orderItemRequest);
        orderRequest.setItems(itemRequests);

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(5);

        when(productService.getProductById(1L)).thenReturn(product);
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(orderRequest);

        assertNotNull(result);
        assertFalse(result.isPaid());
        assertNull(result.getPaymentTime());
        assertEquals(1, result.getItems().size());
    }

    @Test
    public void testCreateOrder_InvalidOrderRequest_ThrowsIllegalArgumentException() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    public void testPayOrder_OrderNotPaid_PaysOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setPaid(false);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.payOrder(1L);

        assertTrue(order.isPaid());
        assertNotNull(order.getPaymentTime());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testPayOrder_OrderAlreadyPaid_ThrowsIllegalStateException() {
        Order order = new Order();
        order.setId(1L);
        order.setPaid(true);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.payOrder(1L));
    }

    @Test
    public void testGetAllOrders_ReturnsListOfOrders() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteOrder_DeletesOrder() {
        Long orderId = 1L;

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testFindByPaidFalseAndCreatedAtBeforeWithItems_ReturnsListOfOrders() {
        LocalDateTime time = LocalDateTime.now();
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findByPaidFalseAndCreatedAtBeforeWithItems(time)).thenReturn(orders);

        List<Order> result = orderService.findByPaidFalseAndCreatedAtBeforeWithItems(time);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByPaidFalseAndCreatedAtBeforeWithItems_NoOrders_ReturnsEmptyList() {
        LocalDateTime time = LocalDateTime.now();
        when(orderRepository.findByPaidFalseAndCreatedAtBeforeWithItems(time)).thenReturn(Collections.emptyList());

        List<Order> result = orderService.findByPaidFalseAndCreatedAtBeforeWithItems(time);

        assertTrue(result.isEmpty());
    }
}