package com.example.foodorderingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewOrders extends AppCompatActivity {


    private RecyclerView orderHistoryRecyclerView;
    private TextView noOrdersText;

    private OrderHistoryAdapter adapter; // Custom adapter for RecyclerView
    private List<OrderModel> orderHistoryList; // Data model for orders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieworders);

        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
        noOrdersText = findViewById(R.id.noOrdersText);

        orderHistoryList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(orderHistoryList);

        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryRecyclerView.setAdapter(adapter);

        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userEmail = auth.getCurrentUser().getEmail();

        if (userEmail != null) {
            String sanitizedEmail = userEmail.replace(".", "_");

            // Fetch orders from Firebase
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child(sanitizedEmail);
            ordersRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            String orderId = orderSnapshot.getKey();
                            Map<String, Object> orderData = (Map<String, Object>) orderSnapshot.getValue();

                            // Parse order data
                            String orderType = (String) orderData.get("orderType");
                            double total = Double.parseDouble(orderData.get("total").toString());
                            List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");

                            List<FoodItem> foodItems = new ArrayList<>();
                            for (Map<String, Object> item : items) {
                                // Retrieve item details from the Firebase snapshot
                                String name = (String) item.get("name"); // Item name as String
                                String cost = (String) item.get("cost"); // Item cost as String
                                int imageResId = R.drawable.logo3; // Default image resource ID

                                // Create FoodItem object with retrieved data
                                foodItems.add(new FoodItem(imageResId, name, cost));
                            }

                            // Add order details along with food items to the order history list
                            orderHistoryList.add(new OrderModel(orderId, orderType, total, foodItems));
                        }

                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                        noOrdersText.setVisibility(View.GONE); // Hide "No orders found" message
                    } else {
                        noOrdersText.setVisibility(View.VISIBLE); // Show "No orders found" message
                    }
                } else {
                    Toast.makeText(this, "Failed to fetch orders!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Log.e("OrderHistory", "Error fetching orders", e));
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }


}