package com.example.milk_order_app.repository;

import com.example.milk_order_app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByProductName(String productName);

}