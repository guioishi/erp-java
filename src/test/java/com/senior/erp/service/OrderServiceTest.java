package com.senior.erp.service;

import com.senior.erp.dto.*;
import com.senior.erp.entity.*;
import com.senior.erp.repository.OrderRepository;
import com.senior.erp.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService service;

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Produto Teste");
        product.setPrice(BigDecimal.valueOf(100));
        product.setType(ProductType.PRODUCT);
        product.setActive(true);
    }

    @Test
    void shouldCreateOrder() {
        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(product.getId());
        itemReq.setQuantity(2);

        OrderRequest request = new OrderRequest();
        request.setDiscount(BigDecimal.TEN);
        request.setItems(List.of(itemReq));

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = service.create(request);

        assertNotNull(response);
        assertEquals(OrderStatus.OPEN, response.getStatus());
        assertEquals(1, response.getItems().size());
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(UUID.randomUUID());
        itemReq.setQuantity(1);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(itemReq));

        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.create(request));
    }

    @Test
    void shouldFindAllWithPagination() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.OPEN);
        order.setTotal(BigDecimal.valueOf(100));

        Page<Order> page = new PageImpl<>(List.of(order));

        when(orderRepository.findAllWithFilter(any(), any()))
                .thenReturn(page);

        Page<OrderResponse> result = service.findAll(new OrderFilter(), PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
    }

    @Test
    void shouldFindById() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        OrderResponse response = service.findById(order.getId());

        assertEquals(order.getId(), response.getId());
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                service.findById(UUID.randomUUID()));
    }

    @Test
    void shouldCloseOrder() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        service.close(order.getId());

        assertEquals(OrderStatus.CLOSED, order.getStatus());
    }

    @Test
    void shouldDeleteOrder() {
        Order order = new Order();
        order.setId(UUID.randomUUID());

        when(orderRepository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        service.delete(order.getId());

        verify(orderRepository).delete(order);
    }
}