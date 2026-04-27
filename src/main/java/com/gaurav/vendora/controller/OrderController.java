package com.gaurav.vendora.controller;

import com.gaurav.vendora.payload.dto.OrderDto;
import com.gaurav.vendora.payload.dto.OrderRequestDto;
import com.gaurav.vendora.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN','CASHIER')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STORE_ADMIN','CASHIER')")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) throws Exception {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrders() throws Exception {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}