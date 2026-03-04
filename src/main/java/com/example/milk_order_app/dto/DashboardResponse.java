package com.example.milk_order_app.dto;

public class DashboardResponse {

    private long totalProducts;
    private long totalOrders;
    private long totalUsers;

    public DashboardResponse(long totalProducts, long totalOrders, long totalUsers) {
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.totalUsers = totalUsers;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public long getTotalUsers() {
        return totalUsers;
    }
}