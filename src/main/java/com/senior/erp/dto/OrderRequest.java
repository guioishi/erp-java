package com.senior.erp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {

    private BigDecimal discount;

    @NotEmpty(message = "Pedido deve ter pelo menos 1 item")
    @Valid
    private List<OrderItemRequest> items;

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}