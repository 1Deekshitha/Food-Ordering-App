package com.example.foodorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class food_item extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        // Get buttons
        Button menuButton = findViewById(R.id.menuButton);
        Button viewOrdersButton = findViewById(R.id.viewOrdersButton);

        // Set click listeners for buttons
        menuButton.setOnClickListener(v -> {
            // Add light zoom animation on button click
            Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
            v.startAnimation(zoomIn);

            // Delay starting new activity until animation ends
            v.postDelayed(() -> {
                Intent intent = new Intent(food_item.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Smooth fade transition
            }, 300); // Animation duration
        });

        viewOrdersButton.setOnClickListener(v -> {
            // Add light zoom animation on button click
            Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
            v.startAnimation(zoomIn);

            // Delay starting new activity until animation ends
            v.postDelayed(() -> {
                Intent intent = new Intent(food_item.this, ViewOrders.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Smooth fade transition
            }, 300); // Animation duration
        });
    }
}