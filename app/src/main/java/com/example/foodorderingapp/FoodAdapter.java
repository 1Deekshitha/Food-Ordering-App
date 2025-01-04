package com.example.foodorderingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context context;
    private List<FoodItem> foodList;
    private List<FoodItem> cartList;

    public FoodAdapter(Context context, List<FoodItem> foodList, List<FoodItem> cartList) {
        this.context = context;
        this.foodList = foodList;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);

        holder.foodImage.setImageResource(foodItem.getImageResId());
        holder.foodName.setText(foodItem.getName());
        holder.foodCost.setText(foodItem.getCost());

        // Add to Cart Button Click Listener
        holder.addToCartButton.setOnClickListener(v -> {
            if (!cartList.contains(foodItem)) {
                cartList.add(foodItem);
                Toast.makeText(context, foodItem.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, foodItem.getName() + " is already in the cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
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
}