package com.gaurav.vendora.service.impl;
import com.gaurav.vendora.domain.PaymentStatus;
import com.gaurav.vendora.domain.OrderStatus;
import com.gaurav.vendora.model.Order;
import com.gaurav.vendora.repository.OrderRepository;
import com.gaurav.vendora.service.PaymentService;
import com.gaurav.vendora.payload.dto.PaymentRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;

    @Override
    public String processPayment(PaymentRequest request) throws Exception {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 🔥 Fake payment logic (simulate success)
        boolean paymentSuccess = true;

        if (paymentSuccess) {
            order.setPaymentStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.CONFIRMED);
        } else {
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);

        return "Payment SUCCESS for Order ID: " + order.getId();
    }
}