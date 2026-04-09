package com.senior.erp.dto;

import com.senior.erp.entity.OrderStatus;
import java.math.BigDecimal;

public class OrderFilter {

    private OrderStatus status;
    private BigDecimal minTotal;
    private BigDecimal maxTotal;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getMinTotal() {
        return minTotal;
    }

    public void setMinTotal(BigDecimal minTotal) {
        this.minTotal = minTotal;
    }

    public BigDecimal getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(BigDecimal maxTotal) {
        this.maxTotal = maxTotal;
    }
}