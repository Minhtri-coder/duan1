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
    private String userId;

    public CartManager(Context context, String userId) {
        pref = context.getSharedPreferences(CART_PREF, Context.MODE_PRIVATE);
        gson = new Gson();
        this.userId = userId;
    }

    private String getUserCartKey() {
        return "cart_" + userId;
    }

    public void addToCart(CartItem item) {
        ArrayList<CartItem> list = getCart();

        for (CartItem c : list) {
            if (c.getProductId().equals(item.getProductId())) {
                c.setQuantity(c.getQuantity() + item.getQuantity());
                saveCart(list);
                return;
            }
        }

        list.add(item);
        saveCart(list);
    }

    public ArrayList<CartItem> getCart() {
        String json = pref.getString(getUserCartKey(), "");
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();

        if (json.isEmpty()) return new ArrayList<>();
        return gson.fromJson(json, type);
    }

    public void saveCart(ArrayList<CartItem> list) {
        pref.edit().putString(getUserCartKey(), gson.toJson(list)).apply();
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : getCart()) {
            total += item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        pref.edit().remove(getUserCartKey()).apply();
    }
}
