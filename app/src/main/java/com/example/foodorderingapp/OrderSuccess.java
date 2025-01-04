package com.example.foodorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        TextView successMessage = findViewById(R.id.successMessage);
        Button goToHomeButton = findViewById(R.id.goToHomeButton);

        // Retrieve total cost for display (if needed)
        double totalCost = getIntent().getDoubleExtra("totalCost", 0);
        successMessage.setText("Your order of ₹" + totalCost + " has been placed successfully!");

        goToHomeButton.setOnClickListener(v -> {
            // Redirect to the home page or main activity
            Intent intent = new Intent(OrderSuccess.this, food_item.class); // Replace MainActivity with your home activity class name
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Optionally auto-redirect to home page after a delay
       // 5 seconds delay
    }
}
