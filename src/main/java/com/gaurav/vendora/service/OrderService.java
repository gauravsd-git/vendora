package com.gaurav.vendora.service;

import com.gaurav.vendora.payload.dto.OrderDto;
import com.gaurav.vendora.payload.dto.OrderRequestDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderRequestDto orderRequestDto) throws Exception;

    List<OrderDto> getAllOrders() throws Exception;

    OrderDto getOrderById(Long id) throws Exception;
}