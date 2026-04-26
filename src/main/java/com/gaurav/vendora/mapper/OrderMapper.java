package com.gaurav.vendora.mapper;

import com.gaurav.vendora.model.Order;
import com.gaurav.vendora.model.OrderItem;
import com.gaurav.vendora.payload.dto.OrderDto;
import com.gaurav.vendora.payload.dto.OrderItemDto;

import java.util.List;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .storeId(order.getStore() != null ? order.getStore().getId() : null)
                .customerName(order.getCustomerName())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(order.getItems() != null ? order.getItems().stream()
                                                  .map(OrderMapper::toItemDto)
                                                  .toList() : List.of())
                .build();
    }

    public static OrderItemDto toItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subTotal(item.getSubTotal())
                .build();
    }
}