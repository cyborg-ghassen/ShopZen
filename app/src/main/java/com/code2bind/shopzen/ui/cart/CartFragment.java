package com.code2bind.shopzen.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.databinding.FragmentCartBinding;
import com.code2bind.shopzen.exceptions.ColumnIndexException;
import com.code2bind.shopzen.exceptions.ItemDoesNotExistsException;
import com.code2bind.shopzen.models.DBHelper;
import com.code2bind.shopzen.models.cart.CartItem;

import java.util.List;


public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private List<CartItem> cartItems;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = binding.cartRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        try {
            DBHelper dbHelper = new DBHelper(inflater.getContext());
            cartItems = dbHelper.getCartItemList();
            CartAdapter adapter = new CartAdapter(cartItems);
            recyclerView.setAdapter(adapter);
        } catch (ColumnIndexException e) {
            e.showDialog(inflater.getContext());
        } catch (ItemDoesNotExistsException e) {
            e.showDialog(inflater.getContext());
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}