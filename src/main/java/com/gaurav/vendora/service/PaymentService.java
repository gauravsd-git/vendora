package com.gaurav.vendora.service;

import com.gaurav.vendora.payload.dto.PaymentRequest;

public interface PaymentService {

    String processPayment(PaymentRequest request) throws Exception;
}