package com.example.foodorderingapp;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private int imageResId;
    private String name;
    private String cost;
    private int quantity = 1; // Default quantity is 1

    public FoodItem(int imageResId, String name, String cost) {
        this.imageResId = imageResId;
        this.name = name;
        this.cost = cost;
    }

    // Getters and Setters
    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Default to 1

}