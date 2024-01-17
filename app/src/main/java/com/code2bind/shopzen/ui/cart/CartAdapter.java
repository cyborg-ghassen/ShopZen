package com.code2bind.shopzen.ui.cart;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.models.cart.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartList;

    public CartAdapter(List<CartItem> cartList){
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(CartViewHolder holder, int position){
        CartItem cartItem = cartList.get(position);

        holder.productName.setText(cartItem.getProduct().getName());
        holder.itemQuantity.setText("Quantité: " + String.valueOf(cartItem.getQuantity()));
        holder.productPrice.setText("Prix: " + String.valueOf(cartItem.getProduct().getPrice() * cartItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView itemQuantity;
        TextView productPrice;

        public CartViewHolder(View itemView){
            super(itemView);
            productName = itemView.findViewById(R.id.product);
            itemQuantity = itemView.findViewById(R.id.quantity);
            productPrice = itemView.findViewById(R.id.price);
        }
    }
}
