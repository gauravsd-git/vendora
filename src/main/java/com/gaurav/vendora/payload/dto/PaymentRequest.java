package com.gaurav.vendora.payload.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private String paymentMethod; // UPI / CARD / CASH
}