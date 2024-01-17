package com.code2bind.shopzen.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.code2bind.shopzen.exceptions.ColumnIndexException;
import com.code2bind.shopzen.exceptions.ItemDoesNotExistsException;
import com.code2bind.shopzen.models.cart.CartItem;
import com.code2bind.shopzen.models.contact.Contact;
import com.code2bind.shopzen.models.product.Product;
import com.code2bind.shopzen.models.shop.Shop;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shopzen.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table product(" +
                "id integer primary key autoincrement," +
                "name varchar(100)," +
                "price float," +
                "stockQuantity integer" +
                ")");
        db.execSQL("create table contact(" +
                "id integer primary key autoincrement," +
                "name varchar(100)," +
                "phone varchar(100)," +
                "email varchar(100)" +
                ")");
        db.execSQL("create table shop(" +
                "id integer primary key autoincrement," +
                "name varchar(100)," +
                "address varchar(256)," +
                "latitude double," +
                "longitude double" +
                ")");
        db.execSQL("create table cartitem(" +
                "id integer primary key autoincrement," +
                "product_id integer," +
                "quantity integer," +
                "foreign key (product_id) references product(id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists product");
        db.execSQL("drop table if exists contact");
        db.execSQL("drop table if exists shop");
        db.execSQL("drop table if exists cartitem");
    }

    public void addProductData(Product product) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "INSERT INTO product(name, price, stockQuantity) VALUES (?, ?, ?)";
        database.execSQL(query, new Object[]{product.getName(), product.getPrice(), product.getStockQuantity()});
        database.close();
    }

    public void addShopData(Shop shop, boolean withMapCoordinates) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (withMapCoordinates){
            String query = "INSERT INTO shop(name, address, latitude, longitude) VALUES (?, ?, ?, ?)";
            database.execSQL(query, new Object[]{shop.getName(), shop.getAddress(), shop.getLatitude(), shop.getLongitude()});
        } else {
            String query = "INSERT INTO shop(name, address) VALUES (?, ?)";
            database.execSQL(query, new Object[]{shop.getName(), shop.getAddress()});
        }
        database.close();
    }

    public void addContactData(Contact contact) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "INSERT INTO contact(name, phone, email) VALUES (?, ?, ?)";
        database.execSQL(query, new Object[]{contact.getName(), contact.getPhone(), contact.getEmail()});
        database.close();
    }

    public Product getProductDetails(int id) throws ColumnIndexException, ItemDoesNotExistsException {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = {"id", "name", "price", "stockQuantity"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = database.query("product", columns, selection, selectionArgs, null, null, null);
        Product product = null;

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int idColumnIndex = cursor.getColumnIndex("id");
                    int nameColumnIndex = cursor.getColumnIndex("name");
                    int priceColumnIndex = cursor.getColumnIndex("price");
                    int stockQuantityColumnIndex = cursor.getColumnIndex("stockQuantity");

                    // Check if the column indexes are valid (not -1)
                    if (idColumnIndex >= 0 && nameColumnIndex >= 0 && priceColumnIndex >= 0 && stockQuantityColumnIndex >= 0) {
                        int productId = cursor.getInt(idColumnIndex);
                        String productName = cursor.getString(nameColumnIndex);
                        double productPrice = cursor.getDouble(priceColumnIndex);
                        int stockQuantity = cursor.getInt(stockQuantityColumnIndex);

                        product = new Product();
                        product.setId(productId);
                        product.setName(productName);
                        product.setPrice(productPrice);
                        product.setStockQuantity(stockQuantity);
                    } else {
                        throw new ColumnIndexException("Check column names if they are called or named correctly.");
                    }
                }
                else {
                    throw new ItemDoesNotExistsException("Product with that id (" + id + ") does not exists.");
                }
            } finally {
                cursor.close();
            }
        }
        database.close();

        return product;
    }

    public List<Product> getProductList(){
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query("product", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double productPrice = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") int stockQuantity = cursor.getInt(cursor.getColumnIndex("stockQuantity"));

                // Add other attributes as needed

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                product.setPrice(productPrice);
                product.setStockQuantity(stockQuantity);

                // Add other attributes as needed

                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
        }
        database.close();
        return productList;
    }

    public List<Shop> getShopList(){
        List<Shop> shopList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query("shop", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int shopId = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String shopName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String shopAddress = cursor.getString(cursor.getColumnIndex("address"));

                // Add other attributes as needed

                Shop shop = new Shop();
                shop.setId(shopId);
                shop.setName(shopName);
                shop.setAddress(shopAddress);

                // Add other attributes as needed

                shopList.add(shop);
            } while (cursor.moveToNext());

            cursor.close();
        }
        database.close();
        return shopList;
    }

    public List<CartItem> getCartItemList() throws ColumnIndexException, ItemDoesNotExistsException{
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query("cartitem", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int cartId = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String productId = cursor.getString(cursor.getColumnIndex("product_id"));
                @SuppressLint("Range") String itemQuantity = cursor.getString(cursor.getColumnIndex("quantity"));

                try {
                    Product product = getProductDetails(Integer.parseInt(productId));
                    CartItem cartItem = new CartItem();
                    cartItem.setId(cartId);
                    cartItem.setProduct(product);
                    cartItem.setQuantity(Integer.parseInt(itemQuantity));
                    cartItems.add(cartItem);
                } catch (ColumnIndexException | ItemDoesNotExistsException | NumberFormatException e) {
                    throw new RuntimeException(e);
                }

            } while (cursor.moveToNext());

            cursor.close();
        }
        database.close();
        return cartItems;
    }

    public void addProductToCart(CartItem item) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "INSERT INTO cartitem(product_id, quantity) VALUES (?, ?)";
        database.execSQL(query, new Object[]{item.getProduct().getId(), item.getQuantity()});
        database.close();
    }

    public List<Shop> getNearbyShops(double userLatitude, double userLongitude) {
        List<Shop> nearbyShops = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query("shop", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int shopId = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String shopName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String shopAddress = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") double shopLatitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                @SuppressLint("Range") double shopLongitude = cursor.getDouble(cursor.getColumnIndex("longitude"));

                // Check if the shop is within a certain distance from the user
                if (calculateDistance(userLatitude, userLongitude, shopLatitude, shopLongitude) < 1000) {
                    // Add the shop to the list of nearby shops
                    Shop shop = new Shop(shopId, shopName, shopAddress, shopLatitude, shopLongitude);
                    nearbyShops.add(shop);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        database.close();
        return nearbyShops;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }
}
