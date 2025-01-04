package com.example.foodorderingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<FoodItem> cartList;
    private UpdateTotalCostCallback updateTotalCostCallback;

    // Constructor
    public CartAdapter(Context context, List<FoodItem> cartList, UpdateTotalCostCallback callback) {
        this.context = context;
        this.cartList = cartList;
        this.updateTotalCostCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = cartList.get(position);

        // Bind data to views
        holder.foodImage.setImageResource(item.getImageResId());
        holder.foodName.setText(item.getName());
        holder.foodCost.setText("₹" + item.getCost());
        holder.quantityInput.setText(String.valueOf(item.getQuantity()));

        // Update item quantity
        holder.quantityInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    int quantity = Integer.parseInt(holder.quantityInput.getText().toString());
                    item.setQuantity(quantity);
                    updateTotalCostCallback.updateTotalCost(cartList);
                } catch (NumberFormatException e) {
                    holder.quantityInput.setText(String.valueOf(item.getQuantity()));
                }
            }
        });

        // Remove item from cart on button click
        holder.addToCartButton.setText("Remove");
        holder.addToCartButton.setOnClickListener(v -> {
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            updateTotalCostCallback.updateTotalCost(cartList);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodCost;
        Button addToCartButton;
        EditText quantityInput;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodCost = itemView.findViewById(R.id.foodCost);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            quantityInput = itemView.findViewById(R.id.quantityInput);
        }
    }

    // Interface for updating total cost
    public interface UpdateTotalCostCallback {
        void updateTotalCost(List<FoodItem> cartItems);
    }
}