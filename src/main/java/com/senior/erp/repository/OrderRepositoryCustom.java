package com.senior.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.senior.erp.dto.OrderFilter;
import com.senior.erp.entity.Order;

public interface OrderRepositoryCustom {
    
    Page<Order> findAllWithFilter(OrderFilter filter, Pageable pageable);
}
    
