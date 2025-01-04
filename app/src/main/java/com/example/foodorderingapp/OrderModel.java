package com.example.foodorderingapp;

import java.util.List;

public class OrderModel {
    private String orderId;
    private String orderType;
    private double total;
    private List<FoodItem> items;

    public OrderModel(String orderId, String orderType, double total, List<FoodItem> items) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.total = total;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public double getTotal() {
        return total;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public List<FoodItem> getFoodItems() {  // This is where you access the list of food items
        return items;
    }
}
