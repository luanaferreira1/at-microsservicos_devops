package com.assessment.product.service;

import com.assessment.product.model.Product;
import com.assessment.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldReturnAllProducts() {
        Product p1 = new Product(1L, "Laptop", 3000.0);
        Product p2 = new Product(2L, "Mouse", 50.0);
        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> result = service.getAllProducts();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnProductById() {
        Product p1 = new Product(1L, "Laptop", 3000.0);
        when(repository.findById(1L)).thenReturn(Optional.of(p1));

        Product result = service.getProductById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getProductById(99L));
    }

    @Test
    void shouldSaveProduct() {
        Product p1 = new Product(null, "Laptop", 3000.0);
        Product savedP1 = new Product(1L, "Laptop", 3000.0);

        when(repository.save(p1)).thenReturn(savedP1);

        Product result = service.saveProduct(p1);

        assertNotNull(result.getId());
        assertEquals("Laptop", result.getName());
    }
}