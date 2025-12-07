package com.assessment.order.controller;

import com.assessment.order.client.ProductClient;
import com.assessment.order.dto.ProductDTO;
import com.assessment.order.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final ProductClient productClient;

    public OrderController(ProductClient productClient) {
        this.productClient = productClient;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam Long productId, @RequestParam String customer) {
        ProductDTO product = productClient.getProductById(productId);

        Order newOrder = new Order(
                System.currentTimeMillis(),
                customer,
                product.getName(),
                product.getPrice()
        );

        return ResponseEntity.ok(newOrder);
    }
}