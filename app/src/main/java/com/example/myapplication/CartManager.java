package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.Model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartManager {

    private Context context;
    private String userId;
    private SharedPreferences pref;
    private Gson gson = new Gson();

    public CartManager(Context context, String userId) {
        this.context = context;
        this.userId = userId;

        pref = context.getSharedPreferences("CART_" + userId, Context.MODE_PRIVATE);
    }

    // ✅ LẤY GIỎ HÀNG
    public ArrayList<CartItem> getCart() {
        String json = pref.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();

        if (json == null) return new ArrayList<>();
        return gson.fromJson(json, type);
    }

    // ✅ LƯU GIỎ HÀNG
    public void saveCart(ArrayList<CartItem> cartList) {
        pref.edit().putString("cart", gson.toJson(cartList)).apply();
    }

    // ✅ ADD TO CART – CHỐNG NULL – KHÔNG CRASH
    public void addToCart(CartItem cartItem) {

        // ✅ CHỐNG NULL ID
        if (cartItem.getProductId() == null) {
            return; // không làm gì nếu ID lỗi
        }

        ArrayList<CartItem> cartList = getCart();
        boolean found = false;

        for (CartItem item : cartList) {

            // ✅ CHỐNG NULL 2 ĐẦU TRƯỚC KHI equals
            if (item.getProductId() != null &&
                    item.getProductId().equals(cartItem.getProductId())) {

                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            cartList.add(cartItem);
        }

        saveCart(cartList);
    }

    // ✅ LẤY TỔNG SỐ LƯỢNG (CHO BADGE)
    public int getTotalQuantity() {
        ArrayList<CartItem> cartList = getCart();
        int total = 0;

        for (CartItem item : cartList) {
            total += item.getQuantity();
        }

        return total;
    }
}
