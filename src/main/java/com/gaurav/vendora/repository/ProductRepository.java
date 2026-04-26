package com.gaurav.vendora.repository;

import com.gaurav.vendora.model.Product;
import com.gaurav.vendora.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStore(Store store);}