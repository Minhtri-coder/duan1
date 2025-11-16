package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Adapter.ProductAdapterAdmin;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.Model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class productAdminTestFragment extends Fragment {
    RecyclerView rectProduct;
    ProductDao productDao;
    ArrayList<Product>listProduct;
    ProductAdapterAdmin productAdapterAdmin;
    FloatingActionButton btnadd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_admin_test, container, false);
        rectProduct = view.findViewById(R.id.recProduct);
        btnadd = view.findViewById(R.id.btnAdd);
        listProduct = new ArrayList<>();
        productDao = new ProductDao();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rectProduct.setLayoutManager(gridLayoutManager);
        productAdapterAdmin = new ProductAdapterAdmin(getContext(),listProduct,productDao);
        rectProduct.setAdapter(productAdapterAdmin);

        loadproduct();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct();
            }
        });

        return view;
    }

    private void AddProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_dialog_add_product,null);
        EditText edtName = view.findViewById(R.id.edtNameProduct);
        EditText edtPrice = view.findViewById(R.id.edtPriceProduct);
        EditText edtImage = view.findViewById(R.id.edtImgProduct);
        EditText edtDec = view.findViewById(R.id.edtDescriptionProduct);
        Button btnAdd = view.findViewById(R.id.btnAddProduct);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString();
                String image = edtImage.getText().toString();
                String dec = edtDec.getText().toString();
                Product product = new Product(name,price,image,dec);
                productDao.Addproduct(product);
                loadproduct();
                dialog.dismiss();
            }
        });
        Button btnCancel  = view.findViewById(R.id.btnCancelProduct);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        dialog.show();
    }

    private void loadproduct() {
        productDao.getAllproduct(new OnSuccessListener<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                listProduct.clear();
                listProduct.addAll(products);
                productAdapterAdmin.notifyDataSetChanged();
            }
        });
    }
}