package com.senior.erp.service;

import com.senior.erp.dto.ProductFilter;
import com.senior.erp.dto.ProductRequest;
import com.senior.erp.entity.Product;
import com.senior.erp.entity.ProductType;
import com.senior.erp.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Produto");
        request.setPrice(BigDecimal.TEN);
        request.setType(ProductType.PRODUCT);

        Product saved = new Product();
        saved.setId(UUID.randomUUID());
        saved.setName(request.getName());
        saved.setPrice(request.getPrice());
        saved.setType(request.getType());
        saved.setActive(true);

        when(repository.save(any(Product.class))).thenReturn(saved);

        var response = service.create(request);

        assertNotNull(response.getId());
        assertEquals("Produto", response.getName());
        assertTrue(response.getActive());
    }

    @Test
    void shouldUpdateProduct() {
        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductRequest request = new ProductRequest();
        request.setName("Novo");
        request.setPrice(BigDecimal.ONE);
        request.setType(ProductType.SERVICE);

        var response = service.update(id, request);

        assertEquals("Novo", response.getName());
        assertEquals(ProductType.SERVICE, response.getType());
    }

    @Test
    void shouldNotUpdateWhenProductHasOrders() {
        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);

        // 👇 CORREÇÃO AQUI
        product.getOrderItems().add(mock(com.senior.erp.entity.OrderItem.class));

        when(repository.findById(id)).thenReturn(Optional.of(product));

        ProductRequest request = new ProductRequest();

        assertThrows(RuntimeException.class, () -> {
            service.update(id, request);
        });
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(product));

        var response = service.findById(id);

        assertEquals(id, response.getId());
    }

    @Test
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.findById(id);
        });
    }

    @Test
    void shouldFindAllWithFilter() {
        Product product = new Product();
        product.setId(UUID.randomUUID());

        Page<Product> page = new PageImpl<>(List.of(product));

        when(repository.findAllWithFilter(any(ProductFilter.class), any(Pageable.class)))
                .thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);

        var result = service.findAll(new ProductFilter(), pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void shouldDeleteProduct() {
        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(product));

        service.delete(id);

        verify(repository).delete(product);
    }

    @Test
    void shouldNotDeleteWhenLinkedToOrder() {
        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);

        // 👇 CORREÇÃO AQUI
        product.getOrderItems().add(mock(com.senior.erp.entity.OrderItem.class));

        when(repository.findById(id)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> {
            service.delete(id);
        });
    }

    @Test
    void shouldToggleActive() {
        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);
        product.setActive(true);

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        var response = service.toggleActive(id);

        assertFalse(response.getActive());
    }
}