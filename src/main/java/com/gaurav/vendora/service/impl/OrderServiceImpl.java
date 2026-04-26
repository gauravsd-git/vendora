package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.domain.OrderStatus;
import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.mapper.OrderMapper;
import com.gaurav.vendora.model.*;
import com.gaurav.vendora.payload.dto.OrderDto;
import com.gaurav.vendora.payload.dto.OrderItemRequestDto;
import com.gaurav.vendora.payload.dto.OrderRequestDto;
import com.gaurav.vendora.repository.OrderRepository;
import com.gaurav.vendora.repository.ProductRepository;
import com.gaurav.vendora.service.OrderService;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Transactional
    @Override
    public OrderDto createOrder(OrderRequestDto orderRequestDto) throws Exception {

        User currentUser = userService.getCurrentUser();

        if (currentUser.getStore() == null) {
            throw new UserException("User is not assigned to any store");
        }

        Store store = currentUser.getStore();

        Order order = new Order();
        order.setStore(store);
        order.setCustomerName(orderRequestDto.getCustomerName());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(0.0);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequestDto itemRequest : orderRequestDto.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStore() == null || !product.getStore().getId().equals(store.getId())) {
                throw new UserException("You are not allowed to use this product");
            }

            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            double price = product.getPrice();
            double subTotal = price * itemRequest.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(price);
            orderItem.setSubTotal(subTotal);

            order.addItem(orderItem);

            orderItems.add(orderItem);
            totalAmount += subTotal;
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toDto(savedOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> getAllOrders() throws Exception {

        User currentUser = userService.getCurrentUser();

        if (currentUser.getStore() == null) {
            throw new UserException("User is not assigned to any store");
        }

        Long storeId = currentUser.getStore().getId();

        return orderRepository.findByStoreId(storeId)
                .stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto getOrderById(Long id) throws Exception {

        User currentUser = userService.getCurrentUser();

        if (currentUser.getStore() == null) {
            throw new UserException("User is not assigned to any store");
        }

        Order order = orderRepository.findByIdAndStoreId(id, currentUser.getStore().getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return OrderMapper.toDto(order);
    }
}