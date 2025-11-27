package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.Model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartManager {

    private static final String CART_PREF = "CART_DATA";
    private SharedPreferences pref;
    private Gson gson;

    public CartManager(Context context) {
        pref = context.getSharedPreferences(CART_PREF, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void addToCart(CartItem item) {
        ArrayList<CartItem> list = getCart();

        for (CartItem c : list) {
            if (c.getName().equals(item.getName())) {
                c.setQuantity(c.getQuantity() + 1);
                saveCart(list);
                return;
            }
        }

        list.add(item);
        saveCart(list);
    }

    public ArrayList<CartItem> getCart() {
        String json = pref.getString("cart", "");
        Type type = new TypeToken<ArrayList<CartItem>>(){}.getType();

        if (json.isEmpty()) return new ArrayList<>();
        return gson.fromJson(json, type);
    }

    public void saveCart(ArrayList<CartItem> list) {
        pref.edit().putString("cart", gson.toJson(list)).apply();
    }

    // ✅ TỔNG SỐ LƯỢNG ĐỂ HIỂN THỊ BADGE
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : getCart()) {
            total += item.getQuantity();
        }
        return total;
    }
}
