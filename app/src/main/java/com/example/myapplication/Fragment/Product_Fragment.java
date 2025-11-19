package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapter.ProductAdapter;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Product_Fragment extends Fragment {
    private RecyclerView recProduct;
    private ArrayList<Product>listProduct;
    private ProductDao productDao;
    private ProductAdapter productAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_, container, false);
        recProduct = view.findViewById(R.id.recProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recProduct.setLayoutManager(gridLayoutManager);
        listProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), listProduct, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onclickProduct(Product product) {
                Fragment OrderFragment = new OderDetails_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",product.getProductId());
                OrderFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framecontent,OrderFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        recProduct.setAdapter(productAdapter);
        productDao = new ProductDao();
        loadProducts();


        return view;
    }

    private void loadProducts() {
    productDao.getAllproduct(products -> {
        listProduct.clear();
        listProduct.addAll(products);
        productAdapter.notifyDataSetChanged();
    });

    }
}