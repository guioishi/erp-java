package com.senior.erp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senior.erp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
}