package com.code2bind.shopzen.ui.shops;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.databinding.FragmentShopsBinding;
import com.code2bind.shopzen.models.DBHelper;
import com.code2bind.shopzen.models.shop.Shop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class ShopFragment extends Fragment {

    private FragmentShopsBinding binding;
    private RecyclerView recyclerView;
    private List<Shop> shopList;
    private List<Shop> nearbyShops;
    private boolean locationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        checkLocationPermission();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShopsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                // Now you can perform actions that require location permission
            } else {
                // Handle the case where the user denied the permission
            }
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch user's location and show nearby shops
        if (locationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            double userLatitude = location.getLatitude();
                            double userLongitude = location.getLongitude();

                            DBHelper helper = new DBHelper(requireContext());
                            nearbyShops = helper.getNearbyShops(userLatitude, userLongitude);
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            // You can request the permission here
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private List<Shop> getSampleNearbyShops() {
        List<Shop> nearbyShops = new ArrayList<>();
        nearbyShops.add(new Shop("Shop 1", "abcd", 37.7749, -122.4194));
        nearbyShops.add(new Shop("Shop 2", "abcde", 37.7837, -122.4086));
        // Add more shops as needed
        return nearbyShops;
    }
}