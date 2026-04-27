package com.gaurav.vendora.service;

import java.util.List;

public interface AnalyticsService {

    Double getTotalSales();

    Long getTotalOrders();

    List<Object[]> getTopProducts();
}