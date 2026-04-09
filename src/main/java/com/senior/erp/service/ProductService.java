package com.senior.erp.service;

import com.senior.erp.dto.ProductFilter;
import com.senior.erp.dto.ProductRequest;
import com.senior.erp.dto.ProductResponse;
import com.senior.erp.entity.Product;
import com.senior.erp.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductResponse create(ProductRequest request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setType(request.getType());
        product.setActive(true);

        return toResponse(repository.save(product));
    }

    public ProductResponse update(UUID id, ProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!product.getOrderItems().isEmpty()) {
            throw new RuntimeException("Produto não pode ser alterado, pois está vinculado a pedidos");
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setType(request.getType());

        return toResponse(repository.save(product));
    }

    public ProductResponse findById(UUID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Page<ProductResponse> findAll(ProductFilter filter, Pageable pageable) {
         return repository.findAllWithFilter(filter, pageable)
            .map(this::toResponse);
    }

    public void delete(UUID id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!product.getOrderItems().isEmpty()) {
            throw new RuntimeException("Produto não pode ser removido, pois está vinculado a pedidos");
        }

        repository.delete(product);
    }

    public ProductResponse toggleActive(UUID id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setActive(!Boolean.TRUE.equals(product.getActive()));

        return toResponse(repository.save(product));
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setType(product.getType());
        response.setActive(product.getActive());
        return response;
    }
}