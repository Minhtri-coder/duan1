package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.Home_Fragment;
import com.example.myapplication.Fragment.Product_Fragment;
import com.example.myapplication.Fragment.Person_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;


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

        // ✅ TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ✅ FRAME CHỨA FRAGMENT
        framecontent = findViewById(R.id.framecontent);
        replaceFragment(new Home_Fragment());

        // ✅ GIỎ HÀNG + BADGE TRÊN TOOLBAR
//        btn_cart = findViewById(R.id.btn_cart);
//        txtCartBadge = findViewById(R.id.txtCartBadge);

        String userId = getSharedPreferences("USER", MODE_PRIVATE)
                .getString("userId", "guest");

        cartManager = new CartManager(this, userId);

        updateCartBadge();

        // ✅ BẤM GIỎ TRÊN TOOLBAR → MỞ CART
        btn_cart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        // ✅ BOTTOM NAVIGATION
        bon = findViewById(R.id.bottomNavigation);
        bon.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.home) {
                fragment = new Home_Fragment();
            }
            else if (id == R.id.product) {
                fragment = new Product_Fragment();
            }
            else if (id == R.id.person) {
                fragment = new Person_Fragment();
            }
            else if (id == R.id.love) {
                // ✅ GIỎ HÀNG DƯỚI BOTTOM → MỞ CART
                startActivity(new Intent(this, CartActivity.class));
                return false; // ✅ KHÔNG LOAD FRAGMENT
            }

            if (fragment != null) replaceFragment(fragment);
            return true;
        });
    }

    // ✅ KHI QUAY LẠI → CẬP NHẬT BADGE
    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    // ✅ CẬP NHẬT BADGE
    private void updateCartBadge() {
        int total = cartManager.getTotalQuantity();

        if (total > 0) {
            txtCartBadge.setText(String.valueOf(total));
            txtCartBadge.setVisibility(TextView.VISIBLE);

            // ✅ ANIMATION NHẢY
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    0.7f, 1.1f,
                    0.7f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleAnimation.setDuration(200);
            txtCartBadge.startAnimation(scaleAnimation);

        } else {
            txtCartBadge.setVisibility(TextView.GONE);
        }
    }


    // ✅ HÀM LOAD FRAGMENT
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framecontent, fragment);
        transaction.commit();
    }
}
