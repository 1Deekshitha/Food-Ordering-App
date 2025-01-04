package com.example.foodorderingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<OrderModel> orderList;

    public OrderHistoryAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.orderIdText.setText("Order ID: " + order.getOrderId());
        holder.orderTypeText.setText("Order Type: " + order.getOrderType());
        holder.totalCostText.setText("Total: ₹" + order.getTotal());

        // Display item names
        StringBuilder itemsText = new StringBuilder();
        for (FoodItem foodItem : order.getFoodItems()) {
            itemsText.append(foodItem.getName()).append("\t\t");
        }
        holder.itemsText.setText(itemsText.toString());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, orderTypeText, totalCostText, itemsText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            orderTypeText = itemView.findViewById(R.id.orderTypeText);
            totalCostText = itemView.findViewById(R.id.totalCostText);
            itemsText = itemView.findViewById(R.id.itemsText); // Add reference to the itemsText TextView
        }
    }
}
