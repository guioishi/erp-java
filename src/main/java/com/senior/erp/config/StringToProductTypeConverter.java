package com.senior.erp.config;

import com.senior.erp.entity.ProductType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToProductTypeConverter implements Converter<String, ProductType> {

    @Override
    public ProductType convert(String source) {
        try {
            return ProductType.valueOf(source.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Tipo inválido: " + source + ". Use PRODUCT ou SERVICE"
            );
        }
    }
}