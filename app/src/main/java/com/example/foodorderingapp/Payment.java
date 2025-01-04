package com.example.foodorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Payment extends AppCompatActivity {

    EditText cardNumberInput, expiryDateInput, cvvInput, upiIdInput;
    Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardNumberInput = findViewById(R.id.cardNumberInput);
        expiryDateInput = findViewById(R.id.expiryDateInput);
        cvvInput = findViewById(R.id.cvvInput);
        upiIdInput = findViewById(R.id.upiIdInput);
        payButton = findViewById(R.id.payButton);

        // Retrieve data from Intent
        ArrayList<FoodItem> cartItems = (ArrayList<FoodItem>) getIntent().getSerializableExtra("cartItems");
        String orderType = getIntent().getStringExtra("orderType");
        double totalCost = getIntent().getDoubleExtra("totalCost", 0);

        payButton.setOnClickListener(v -> {
            String cardNumber = cardNumberInput.getText().toString().trim();
            String expiryDate = expiryDateInput.getText().toString().trim();
            String cvv = cvvInput.getText().toString().trim();
            String upiId = upiIdInput.getText().toString().trim();

            if (!cardNumber.isEmpty() && !expiryDate.isEmpty() && !cvv.isEmpty()) {
                if (!isValidCardNumber(cardNumber)) {
                    Toast.makeText(Payment.this, "Invalid card number format.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidCVV(cvv)) {
                    Toast.makeText(Payment.this, "Invalid CVV format. It must be 3 digits.", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveOrderToFirebase(cartItems, orderType, totalCost);
                navigateToSuccessScreen(totalCost);
            } else if (!upiId.isEmpty()) {
                if (!isValidUPIId(upiId)) {
                    Toast.makeText(Payment.this, "Invalid UPI ID format.", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveOrderToFirebase(cartItems, orderType, totalCost);
                navigateToSuccessScreen(totalCost);
            } else {
                Toast.makeText(Payment.this, "Please provide valid payment details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidCardNumber(String cardNumber) {
        // Regex for credit card: must be 13 to 19 digits
        return cardNumber.matches("^[0-9]{13,19}$");
    }

    private boolean isValidCVV(String cvv) {
        // Regex for CVV: must be exactly 3 digits
        return cvv.matches("^[0-9]{3}$");
    }

    private boolean isValidUPIId(String upiId) {
        // Regex for UPI ID: must follow the format `xyz@bank`
        return upiId.matches("^[\\w.]+@[\\w]+$");
    }

    private void saveOrderToFirebase(List<FoodItem> cartItems, String orderType, double total) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;

        if (userEmail != null) {
            String sanitizedEmail = userEmail.replace(".", "_");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ordersRef = database.getReference("orders");
            DatabaseReference counterRef = database.getReference("order_counter");

            counterRef.child(sanitizedEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    long lastOrderId = task.getResult().exists() ? task.getResult().getValue(Long.class) : 2025000;
                    long newOrderId = lastOrderId + 1;

                    HashMap<String, Object> orderData = new HashMap<>();
                    orderData.put("orderType", orderType);
                    orderData.put("total", total);

                    List<HashMap<String, Object>> itemsList = new ArrayList<>();
                    for (FoodItem item : cartItems) {
                        HashMap<String, Object> itemMap = new HashMap<>();
                        itemMap.put("name", item.getName());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("cost", item.getCost());
                        itemsList.add(itemMap);
                    }
                    orderData.put("items", itemsList);

                    ordersRef.child(sanitizedEmail).child(String.valueOf(newOrderId)).setValue(orderData)
                            .addOnCompleteListener(orderTask -> {
                                if (orderTask.isSuccessful()) {
                                    counterRef.child(sanitizedEmail).setValue(newOrderId);
                                    Toast.makeText(Payment.this, "Order saved successfully with ID: " + newOrderId, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Payment.this, "Failed to save order.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "Failed to retrieve order counter.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToSuccessScreen(double totalCost) {
        Intent intent = new Intent(Payment.this, OrderSuccess.class);
        intent.putExtra("totalCost", totalCost);
        startActivity(intent);
        finish();
    }
}
