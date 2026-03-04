package com.example.milk_order_app.controller;

import com.example.milk_order_app.dto.OrderResponse;
import com.example.milk_order_app.entity.AppUser;
import com.example.milk_order_app.entity.Order;
import com.example.milk_order_app.repository.OrderRepository;
import com.example.milk_order_app.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Transactional
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // ==========================
    // ✅ CREATE ORDER
    // ==========================
    @PostMapping("/create")
    public Order createOrder(@RequestBody Order orderRequest) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        orderRequest.setOrderDate(LocalDateTime.now());
        orderRequest.setUser(user);

        return orderRepository.save(orderRequest);
    }

    // ==========================
    // ✅ GET ALL ORDERS (DTO)
    // ==========================
    @GetMapping("/getallorders")
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getUser().getUsername(),   // ✅ Username
                        order.getProductName(),
                        order.getQuantity(),
                        order.getTotalPrice(),
                        order.getOrderDate(),
                        order.getDeliveryAddress()
                ))
                .toList();
    }

    // ==========================
    // ✅ GET ORDERS BY USER ID
    // ==========================
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // ==========================
    // ✅ UPDATE ORDER
    // ==========================
    @PutMapping("/updateorder/{orderId}")
    public Order updateOrder(@PathVariable Long orderId,
                             @RequestBody Order updatedOrder) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setProductName(updatedOrder.getProductName());
        order.setQuantity(updatedOrder.getQuantity());
        order.setTotalPrice(updatedOrder.getTotalPrice());
        order.setDeliveryAddress(updatedOrder.getDeliveryAddress());

        return orderRepository.save(order);
    }

    // ==========================
    // ✅ DELETE ORDER
    // ==========================
    @DeleteMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {

        orderRepository.deleteById(orderId);

        return "Order deleted successfully";
    }
}