package com.senior.erp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.senior.erp.dto.OrderFilter;
import com.senior.erp.dto.OrderItemRequest;
import com.senior.erp.dto.OrderItemResponse;
import com.senior.erp.dto.OrderRequest;
import com.senior.erp.dto.OrderResponse;
import com.senior.erp.entity.Order;
import com.senior.erp.entity.OrderItem;
import com.senior.erp.entity.OrderStatus;
import com.senior.erp.entity.Product;
import com.senior.erp.entity.ProductType;
import com.senior.erp.repository.OrderRepository;
import com.senior.erp.repository.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {

        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setDiscount(request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO);

        BigDecimal totalProducts = BigDecimal.ZERO;
        BigDecimal totalServices = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (!product.getActive()) {
                throw new RuntimeException("Produto desativado não pode ser adicionado");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice());
            item.setType(product.getType());

            BigDecimal totalItem = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            if (product.getType() == ProductType.PRODUCT) {
                totalProducts = totalProducts.add(totalItem);
            } else {
                totalServices = totalServices.add(totalItem);
            }

            order.getItems().add(item);
        }

        BigDecimal discountValue = totalProducts
                .multiply(order.getDiscount())
                .divide(BigDecimal.valueOf(100));

        BigDecimal totalFinal = totalProducts
                .subtract(discountValue)
                .add(totalServices);

        order.setTotal(totalFinal);

        return toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse update(UUID id, OrderRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (order.getStatus() == OrderStatus.CLOSED) {
            throw new RuntimeException("Pedido fechado não pode ser alterado");
        }

        order.getItems().clear();

        order.setDiscount(request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO);

        BigDecimal totalProducts = BigDecimal.ZERO;
        BigDecimal totalServices = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (!product.getActive()) {
                throw new RuntimeException("Produto desativado não pode ser adicionado");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice());
            item.setType(product.getType());

            BigDecimal totalItem = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            if (product.getType() == ProductType.PRODUCT) {
                totalProducts = totalProducts.add(totalItem);
            } else {
                totalServices = totalServices.add(totalItem);
            }

            order.getItems().add(item);
        }

        BigDecimal discountValue = totalProducts
                .multiply(order.getDiscount())
                .divide(BigDecimal.valueOf(100));

        BigDecimal totalFinal = totalProducts
                .subtract(discountValue)
                .add(totalServices);

        order.setTotal(totalFinal);

        return toResponse(orderRepository.save(order));
    }

    public Page<OrderResponse> findAll(OrderFilter filter, Pageable pageable) {
        return orderRepository.findAllWithFilter(filter, pageable)
            .map(this::toResponse);
    }

    public OrderResponse findById(UUID id) {
        return orderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Transactional
    public void close(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(OrderStatus.CLOSED);
    }

    public void delete(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        orderRepository.delete(order);
    }

    private OrderResponse toResponse(Order order) {

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());

        List<OrderItemResponse> items = order.getItems().stream().map(item -> {
            OrderItemResponse dto = new OrderItemResponse();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            System.out.println(item.getType());
            dto.setType(item.getType());
            return dto;
        }).toList();
        response.setItems(items);

        return response;
    }
}