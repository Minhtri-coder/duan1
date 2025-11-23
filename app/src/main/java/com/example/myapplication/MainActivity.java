package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.example.myapplication.Fragment.Home_Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.Product_Fragment;
import com.example.myapplication.Fragment.Person_Fragment;
import com.example.myapplication.Fragment.Cart_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bon;
    FrameLayout framecontent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Ẩn title
        framecontent= findViewById(R.id.framecontent);
        replaceFragment(new Home_Fragment());
        bon = findViewById(R.id.bottomNavigation);
        bon.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new Home_Fragment();
                int id=item.getItemId();
                    if(id==R.id.home){
                        Toast.makeText(MainActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
                        fragment = new Home_Fragment();
                    }
                    else if (id== R.id.product) {
                        Toast.makeText(MainActivity.this, "San Pham", Toast.LENGTH_SHORT).show();
                        fragment= new Product_Fragment();

                    }
                    else if(id == R.id.person){
                        Toast.makeText(MainActivity.this, "nguoi dung", Toast.LENGTH_SHORT).show();
                        fragment= new Person_Fragment();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Yeu thich", Toast.LENGTH_SHORT).show();
                        fragment= new Cart_fragment();
                    }
                // Nếu fragment được chọn thì hiển thị nó
                if (fragment != null) {
                    replaceFragment(fragment);
                    return true;
                }
                return false;
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framecontent, fragment);
        transaction.commit();

    }

}
