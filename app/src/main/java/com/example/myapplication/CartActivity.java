package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CartAdapter;
import com.example.myapplication.Model.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    RecyclerView rcvCart;
    TextView txtSubtotal;
    ImageView btnBack;

    ArrayList<CartItem> cartList;
    CartAdapter adapter;
    CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rcvCart = findViewById(R.id.rcvCart);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        btnBack = findViewById(R.id.btnBack);

        String userId = getSharedPreferences("USER", MODE_PRIVATE)
                .getString("userId", "guest");

        cartManager = new CartManager(this, userId);
        cartList = cartManager.getCart();

        adapter = new CartAdapter(this, cartList, () -> {
            cartManager.saveCart(cartList);
            updateSubtotal();
        });

        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(adapter);
        updateSubtotal();

        btnBack.setOnClickListener(v -> {
            finish();
        });
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
