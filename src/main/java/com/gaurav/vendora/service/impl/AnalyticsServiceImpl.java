package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.repository.OrderItemRepository;
import com.gaurav.vendora.repository.OrderRepository;
import com.gaurav.vendora.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Double getTotalSales() {
        Double revenue = orderRepository.getTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public Long getTotalOrders() {
        Long count = orderRepository.getTotalOrders();
        return count != null ? count : 0L;
    }

    @Override
    public List<Object[]> getTopProducts() {
        return orderItemRepository.getTopSellingProducts();
    }
}