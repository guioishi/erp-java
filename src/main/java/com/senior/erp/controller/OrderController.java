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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/orders")
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @Operation(summary = "Criar pedido", description = "Cria um novo pedido com produtos e/ou serviços")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Listar pedidos", description = "Lista pedidos com paginação e filtros opcionais")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> list(OrderFilter filter, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(filter, pageable));
    }

    @Operation(summary = "Buscar pedido por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Atualizar pedido", description = "Atualiza um pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido atualizado"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid OrderRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Fechar pedido", description = "Altera o status do pedido para CLOSED")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido fechado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> close(@PathVariable UUID id) {
        service.close(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletar pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido removido"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}