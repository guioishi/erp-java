package com.senior.erp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senior.erp.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {
}