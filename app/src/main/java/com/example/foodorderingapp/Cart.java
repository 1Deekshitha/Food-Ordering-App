package com.example.foodorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.FoodItem;
import com.example.foodorderingapp.R;

import java.io.Serializable;
import java.util.List;

public class Cart extends AppCompatActivity {


    RecyclerView cartRecyclerView;
    TextView totalCostText;
    RadioGroup orderTypeRadioGroup;
    Button placeOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalCostText = findViewById(R.id.totalCostText);
        orderTypeRadioGroup = findViewById(R.id.orderTypeRadioGroup);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the passed cart items
        List<FoodItem> cartItems = (List<FoodItem>) getIntent().getSerializableExtra("cartItems");

        if (cartItems != null) {
            // Initialize the adapter with a callback to update total cost
            CartAdapter adapter = new CartAdapter(this, cartItems, this::updateTotalCost);
            cartRecyclerView.setAdapter(adapter);
            updateTotalCost(cartItems); // Initial calculation
        }
        placeOrderButton.setOnClickListener(v -> {
            int selectedRadioId = orderTypeRadioGroup.getCheckedRadioButtonId();
            if (selectedRadioId == -1) {
                // No RadioButton is selected
                Toast.makeText(this, "Please select Dine In or Takeaway", Toast.LENGTH_SHORT).show();
            } else {
                // Get selected order type
                RadioButton selectedRadioButton = findViewById(selectedRadioId);
                String orderType = selectedRadioButton.getText().toString();

                // Navigate to Order Summary page
                Intent intent = new Intent(this, Order.class);
                intent.putExtra("cartItems", (Serializable) cartItems);
                intent.putExtra("orderType", orderType);
                startActivity(intent);
            }
        });
    }

    // Method to calculate and update total cost
    private void updateTotalCost(List<FoodItem> cartItems) {
        double total = 0;
        for (FoodItem item : cartItems) {
            int cost = Integer.parseInt(item.getCost());
            total += cost * item.getQuantity();
        }
        totalCostText.setText("Total Cost: ₹" + total);
    }
}