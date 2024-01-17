package com.code2bind.shopzen.ui.products;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.code2bind.shopzen.R;
import com.code2bind.shopzen.exceptions.ColumnIndexException;
import com.code2bind.shopzen.exceptions.ItemDoesNotExistsException;
import com.code2bind.shopzen.models.cart.CartItem;
import com.code2bind.shopzen.models.product.Product;
import com.code2bind.shopzen.models.DBHelper;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Mettez à jour les vues de l'élément de liste avec les détails du produit
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
        holder.productStockTextView.setText(String.format(Locale.getDefault(), "Stock: %d", product.getStockQuantity()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productStockTextView;
        EditText quantity;
        Button addToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.name);
            productPriceTextView = itemView.findViewById(R.id.price);
            productStockTextView = itemView.findViewById(R.id.stock);
            quantity = itemView.findViewById(R.id.quantity);
            addToCart = itemView.findViewById(R.id.addToCart);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Product selectedProduct = productList.get(position);
                        try {
                            DBHelper dbHelper = new DBHelper(itemView.getContext());
                            Product detailedProduct = dbHelper.getProductDetails(selectedProduct.getId());
                            addToCart(detailedProduct);
                        } catch (ColumnIndexException e) {
                            e.showDialog(itemView.getContext());
                        } catch (ItemDoesNotExistsException e) {
                            e.showDialog(itemView.getContext());
                        }
                    }
                }

                private void addToCart(Product selectedProduct) {
                    DBHelper helper = new DBHelper(itemView.getContext());
                    helper.addProductToCart(new CartItem(selectedProduct, Integer.parseInt(String.valueOf(quantity.getText()))));
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Added to Cart");
                    builder.setMessage("Product successfully added to cart.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the dialog if needed
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }
}
