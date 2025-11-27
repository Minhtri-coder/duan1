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
    ImageView btnBack;

    ArrayList<CartItem> cartList;
    CartAdapter adapter;

    SharedPreferences pref;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rcvCart = findViewById(R.id.rcvCart);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        btnBack = findViewById(R.id.btnBack);

        pref = getSharedPreferences("CART_DATA", MODE_PRIVATE);

        loadCart();

        adapter = new CartAdapter(this, cartList, () -> {
            saveCart();
            updateSubtotal();
        });

        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(adapter);
        updateSubtotal();

        // ✅ BACK VỀ HOME
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void loadCart() {
        String json = pref.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();

        if (json == null) cartList = new ArrayList<>();
        else cartList = gson.fromJson(json, type);
    }

    private void saveCart() {
        pref.edit().putString("cart", gson.toJson(cartList)).apply();
    }

    private void updateSubtotal() {
        double total = 0;
        for (CartItem i : cartList) {
            total += i.getPrice() * i.getQuantity();
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        txtSubtotal.setText(formatter.format(total) + " ₫");
    }
}
