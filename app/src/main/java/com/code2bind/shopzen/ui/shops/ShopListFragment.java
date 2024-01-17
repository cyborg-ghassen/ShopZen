package com.code2bind.shopzen.ui.shops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.models.DBHelper;
import com.code2bind.shopzen.models.shop.Shop;

import java.util.List;

public class ShopListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Shop> shopList;

    private List<Shop> nearbyShops;

    public void setNearbyShops(List<Shop> nearbyShops) {
        this.nearbyShops = nearbyShops;
    }


    public ShopListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop_list, container, false);

        recyclerView = root.findViewById(R.id.shopRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (nearbyShops == null){
            DBHelper helper = new DBHelper(requireContext());
            shopList = helper.getShopList();
            ShopAdapter adapter = new ShopAdapter(shopList);
            recyclerView.setAdapter(adapter);
        }
        else {
            ShopAdapter adapter = new ShopAdapter(nearbyShops);
            recyclerView.setAdapter(adapter);
        }

        return root;
    }
}
