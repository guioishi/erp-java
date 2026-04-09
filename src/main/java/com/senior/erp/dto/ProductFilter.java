package com.senior.erp.dto;

import com.senior.erp.entity.ProductType;

public class ProductFilter {

    private String name;
    private ProductType type;
    private Boolean active;

    public String getName() {
        return name;
    }

    public ProductType getType() {
        return type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}