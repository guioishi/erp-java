package com.senior.erp.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.senior.erp.service.OrderService;
import com.senior.erp.dto.OrderFilter;
import com.senior.erp.dto.OrderRequest;
import com.senior.erp.dto.OrderResponse;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> list(OrderFilter filter, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid OrderRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> close(@PathVariable UUID id) {
        service.close(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}