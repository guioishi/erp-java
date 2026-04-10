package com.senior.erp.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductType {
    PRODUCT,
    SERVICE;

    @JsonCreator
    public static ProductType from(String value) {
        return ProductType.valueOf(value.toUpperCase());
    }
}
