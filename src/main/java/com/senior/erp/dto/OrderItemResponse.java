package com.senior.erp.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.senior.erp.entity.ProductType;

public class OrderItemResponse {

    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private ProductType type; 

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductType getType() { 
        return type; 
    }

    public void setType(ProductType type) { 
        this.type = type; 
    }
}