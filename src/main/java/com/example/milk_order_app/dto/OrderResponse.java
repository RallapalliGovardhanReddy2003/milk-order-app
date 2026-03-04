package com.example.milk_order_app.dto;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private String username;
    private String productName;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private String deliveryAddress;

    public OrderResponse(Long id,
                         String username,
                         String productName,
                         Integer quantity,
                         Double totalPrice,
                         LocalDateTime orderDate,
                         String deliveryAddress) {

        this.id = id;
        this.username = username;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public Double getTotalPrice() { return totalPrice; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getDeliveryAddress() { return deliveryAddress; }
}