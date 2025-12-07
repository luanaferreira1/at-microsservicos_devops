package com.assessment.order.controller;

import com.assessment.order.client.ProductClient;
import com.assessment.order.dto.ProductDTO;
import com.assessment.order.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderController orderController;

    @Test
    void shouldCreateOrderSuccessfully() {
        Long productId = 1L;
        ProductDTO mockProduct = new ProductDTO(1L, "iPhone", 5000.0);

        when(productClient.getProductById(productId)).thenReturn(mockProduct);

        ResponseEntity<Order> response = orderController.createOrder(productId, "Maria");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("iPhone", response.getBody().getProductName());
        assertEquals(5000.0, response.getBody().getAmountPaid());
        assertEquals("Maria", response.getBody().getCustomerName());
    }
}