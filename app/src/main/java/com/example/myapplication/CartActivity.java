package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapter.CartAdapter;
import com.example.myapplication.Model.CartItem;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    RecyclerView rcvCart;
    TextView txtSubtotal;
    ImageView btnBack;   // ✅ NÚT QUAY VỀ
    MaterialButton btncheckout;
//    ImageView btnBack;
    ArrayList<CartItem> cartList;
    CartAdapter adapter;
    CartManager cartManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        rcvCart = findViewById(R.id.rcvCart);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        btnBack = findViewById(R.id.btnBack);   // ✅ NÚT BACK
        btncheckout= findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cartManager = new CartManager(this, userId);
        cartList = cartManager.getCart();

        adapter = new CartAdapter(this, cartList, () -> {
            cartManager.saveCart(cartList);
            updateSubtotal();
            LocalBroadcastManager.getInstance(CartActivity.this)
                    .sendBroadcast(new Intent("UPDATE_BADGE"));
        });

        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(adapter);
        updateSubtotal();

        btnBack.setOnClickListener(v -> {
            finish();
        });
        // su kien bam thanh toan
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, Payment_activity.class);
                intent.putExtra("tongtien", txtSubtotal.getText().toString());
                // ✅ Gửi cả danh sách CartItemxx
                intent.putExtra("cartList", new Gson().toJson(cartList));
                startActivity(intent);
            }
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
