package com.code2bind.shopzen.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code2bind.shopzen.databinding.FragmentProductsBinding;
import com.code2bind.shopzen.models.DBHelper;
import com.code2bind.shopzen.models.product.Product;

import java.util.List;

public class ProductFragment extends Fragment {

    private FragmentProductsBinding binding;
    private List<Product> productList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.productRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        DBHelper helper = new DBHelper(inflater.getContext());

        productList = helper.getProductList();

        ProductAdapter adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Product selectedProduct = productList.get(position);
                navigateToProductDetails(selectedProduct, container);
            }
        });

        return root;
    }

    private void navigateToProductDetails(Product selectedProduct, ViewGroup container) {
        if (selectedProduct != null) {
            ProductDetailsFragment productDetailsFragment = ProductDetailsFragment.newInstance(selectedProduct);

            // Get the FragmentManager
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            // Begin a FragmentTransaction
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Replace the current fragment with the product details fragment
            transaction.replace(container.getId(), productDetailsFragment);
            transaction.addToBackStack(null);  // Optional: Add to back stack for fragment navigation history

            // Commit the transaction
            transaction.commit();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}