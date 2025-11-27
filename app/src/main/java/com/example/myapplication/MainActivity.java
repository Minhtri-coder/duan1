package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.Home_Fragment;
import com.example.myapplication.Fragment.Product_Fragment;
import com.example.myapplication.Fragment.Person_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bon;
    FrameLayout framecontent;
    ImageButton btn_cart;
    TextView txtCartBadge;

    CartManager cartManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        framecontent = findViewById(R.id.framecontent);
        replaceFragment(new Home_Fragment());

        btn_cart = findViewById(R.id.btn_cart);
        txtCartBadge = findViewById(R.id.txtCartBadge);

        cartManager = new CartManager(this);
        updateCartBadge();

        btn_cart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        bon = findViewById(R.id.bottomNavigation);
        bon.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = new Home_Fragment();
            int id = item.getItemId();

            if (id == R.id.home) fragment = new Home_Fragment();
            else if (id == R.id.product) fragment = new Product_Fragment();
            else if (id == R.id.person) fragment = new Person_Fragment();

            replaceFragment(fragment);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge(); // ✅ CẬP NHẬT KHI QUAY LẠI
    }

    private void updateCartBadge() {
        int total = cartManager.getTotalQuantity();
        if (total > 0) {
            txtCartBadge.setText(String.valueOf(total));
            txtCartBadge.setVisibility(TextView.VISIBLE);
        } else {
            txtCartBadge.setVisibility(TextView.GONE);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framecontent, fragment);
        transaction.commit();
    }
}
