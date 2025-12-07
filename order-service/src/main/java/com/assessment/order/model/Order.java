package com.assessment.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private Long orderId;
    private String customerName;
    private String productName;
    private Double amountPaid;
}