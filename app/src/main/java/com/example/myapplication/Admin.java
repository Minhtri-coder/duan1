package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.Admin.Custom_statistics_Fragment;
import com.example.myapplication.Fragment.Home_Fragment;
import com.example.myapplication.Fragment.Person_Fragment;
import com.example.myapplication.Fragment.Product_Fragment;
import com.example.myapplication.Fragment.Admin.Product_statistics_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin extends AppCompatActivity {
    BottomNavigationView btnadmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        btnadmin= findViewById(R.id.bottomNavigation_admin);
        btnadmin.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new Home_Fragment();
                int id=item.getItemId();
                if(id==R.id.category){

                    fragment = new Home_Fragment();
                } else if (id== R.id.product) {
                    fragment = new Product_Fragment();
                } else if (id== R.id.product_statistics) {

                    fragment= new Product_statistics_Fragment();

                }
                else if(id == R.id.user_statistics){

                    fragment= new Custom_statistics_Fragment();
                }
                else{

                    fragment= new Person_Fragment();
                }
                // Nếu fragment được chọn thì hiển thị nó
                if (fragment != null) {
                    replaceFragment(fragment);
                    return true;
                }
                return false;
            }
            private void replaceFragment(Fragment fragment) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framecontent, fragment);
                transaction.commit();
            }
        });
    }
}