package com.example.milk_order_app.controller;


import com.example.milk_order_app.dto.DashboardResponse;
import com.example.milk_order_app.repository.ProductRepository;
import com.example.milk_order_app.repository.OrderRepository;
import com.example.milk_order_app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/summary")
    public DashboardResponse getDashboardSummary() {

        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        long totalUsers = userRepository.count();

        return new DashboardResponse(totalProducts, totalOrders, totalUsers);
    }
}