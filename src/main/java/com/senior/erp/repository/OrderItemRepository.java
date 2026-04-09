package com.senior.erp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senior.erp.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    boolean existsByProductId(UUID productId);
}