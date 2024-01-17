package com.code2bind.shopzen.ui.shops;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.models.shop.Shop;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    private List<Shop> shopList;

    public ShopAdapter(List<Shop> shopList){
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    public void onBindViewHolder(ShopViewHolder holder, int position){
        Shop shop = shopList.get(position);

        holder.shopName.setText(shop.getName());
        holder.shopAddress.setText(shop.getAddress());
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        TextView shopAddress;

        public ShopViewHolder(View itemView){
            super(itemView);
            shopName = itemView.findViewById(R.id.name);
            shopAddress = itemView.findViewById(R.id.address);
        }
    }
}
