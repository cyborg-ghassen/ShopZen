package com.code2bind.shopzen.ui.products;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.models.product.Product;

public class ProductDetailsFragment extends Fragment {
    public static final String ARG_PRODUCT = "selectedProduct";

    public static ProductDetailsFragment newInstance(Product product) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, (Parcelable) product);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Retrieve the selected product
        Product selectedProduct = getArguments().getParcelable(ARG_PRODUCT);

        // TODO: Populate the UI with details of the selected product
        if (selectedProduct != null) {
            TextView productNameTextView = view.findViewById(R.id.productNameTextView);
            TextView productPriceTextView = view.findViewById(R.id.productPriceTextView);
            TextView productStockTextView = view.findViewById(R.id.productStockTextView);

            productNameTextView.setText(selectedProduct.getName());
            productPriceTextView.setText(String.format("$%.2f", selectedProduct.getPrice()));
            productStockTextView.setText(String.format("Stock: %d", selectedProduct.getStockQuantity()));
        }

        return view;
    }

}
