package com.senior.erp.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.senior.erp.entity.OrderStatus;

public class OrderResponse {

    private UUID id;
    private OrderStatus status;
    private BigDecimal total;
    private List<OrderItemResponse> items;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}