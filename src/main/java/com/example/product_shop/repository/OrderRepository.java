package com.example.product_shop.repository;

import com.example.product_shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.paid = false AND o.createdAt < :time")
    List<Order> findByPaidFalseAndCreatedAtBeforeWithItems(@Param("time") LocalDateTime time);
}