package com.senior.erp.repository;

import com.senior.erp.dto.ProductFilter;
import com.senior.erp.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> findAllWithFilter(ProductFilter filter, Pageable pageable);
}