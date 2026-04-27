package com.gaurav.vendora.controller;

import com.gaurav.vendora.payload.dto.PaymentRequest;
import com.gaurav.vendora.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN','CASHIER')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public String processPayment(@RequestBody PaymentRequest request) throws Exception {
        return paymentService.processPayment(request);
    }
}