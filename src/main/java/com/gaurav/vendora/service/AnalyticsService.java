package com.gaurav.vendora.service;

import com.gaurav.vendora.exceptions.UserException;

import java.util.List;

public interface AnalyticsService {

    Double getTotalSales() throws UserException;

    Long getTotalOrders() throws UserException;

    List<Object[]> getTopProducts() throws UserException;
}