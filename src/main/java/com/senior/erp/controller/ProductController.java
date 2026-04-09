package com.senior.erp.controller;

import com.senior.erp.dto.ProductFilter;
import com.senior.erp.dto.ProductRequest;
import com.senior.erp.dto.ProductResponse;
import com.senior.erp.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Gerenciamento de produtos")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar produto")
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Lista produtos com filtro, paginação e ordenação")
    public ResponseEntity<Page<ProductResponse>> list(
            ProductFilter filter,
            @Parameter(description = "Paginação e ordenação")
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(service.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid ProductRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    @Operation(summary = "Ativar/Desativar produto")
    public ResponseEntity<ProductResponse> toggleActive(@PathVariable UUID id) {
        return ResponseEntity.ok(service.toggleActive(id));
    }
}