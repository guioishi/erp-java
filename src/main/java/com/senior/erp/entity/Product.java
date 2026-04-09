package com.senior.erp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private Boolean active = true;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<OrderItem> getOrderItems() { return orderItems; }
}