package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CartAdapter;
import com.example.myapplication.Model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    RecyclerView rcvCart;
    TextView txtSubtotal;
    ImageView btnBack;   // ✅ NÚT QUAY VỀ

    ArrayList<CartItem> cartList;
    CartAdapter adapter;

    SharedPreferences pref;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // ✅ Ánh xạ
        rcvCart = findViewById(R.id.rcvCart);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        btnBack = findViewById(R.id.btnBack);   // ✅ NÚT BACK

        pref = getSharedPreferences("CART_DATA", MODE_PRIVATE);

        loadCart();

        adapter = new CartAdapter(this, cartList, () -> {
            saveCart();
            updateSubtotal();
        });

        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(adapter);

        updateSubtotal();

        // ✅ SỰ KIỆN QUAY VỀ HOME
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // ✅ đóng CartActivity
        });
    }

    // ================= LOAD GIỎ HÀNG =================
    private void loadCart() {
        String json = pref.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();

        if (json == null) {
            cartList = new ArrayList<>();
        } else {
            cartList = gson.fromJson(json, type);
        }
    }

    // ================= LƯU GIỎ HÀNG =================
    private void saveCart() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("cart", gson.toJson(cartList));
        editor.apply();
    }

    // ================= TÍNH TỔNG TIỀN =================
    private void updateSubtotal() {
        double total = 0;
        for (CartItem i : cartList) {
            total += i.getPrice() * i.getQuantity();
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        txtSubtotal.setText(formatter.format(total) + " ₫");
    }
}
