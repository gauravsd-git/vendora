package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.repository.OrderItemRepository;
import com.gaurav.vendora.repository.OrderRepository;
import com.gaurav.vendora.service.AnalyticsService;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;

    private Long getStoreId() throws UserException {
        User user = userService.getCurrentUser();

        if (user.getStore() == null) {
            throw new UserException("Store not found for current user");
        }

        return user.getStore().getId();
    }

    @Override
    public Double getTotalSales() throws UserException {
        Long storeId = getStoreId();
        Double revenue = orderRepository.getTotalRevenueByStore(storeId);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public Long getTotalOrders() throws UserException {
        Long storeId = getStoreId();
        Long count = orderRepository.getTotalOrdersByStore(storeId);
        return count != null ? count : 0L;
    }

    @Override
    public List<Object[]> getTopProducts() throws UserException {
        Long storeId = getStoreId();
        return orderItemRepository.getTopSellingProductsByStore(storeId);
    }
}