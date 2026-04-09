package com.senior.erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class OrderItemRequest {

    @NotNull(message = "Produto é obrigatório")
    private UUID productId;

    @Min(value = 1, message = "Quantidade deve ser no mínimo 1")
    private int quantity;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}