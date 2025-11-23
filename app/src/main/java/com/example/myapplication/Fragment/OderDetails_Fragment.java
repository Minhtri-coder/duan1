package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class OderDetails_Fragment extends Fragment {
    ImageView img;
    TextView txtName,txtDec,txtPrice;
    ProductDao productDao;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details_, container, false);
        img = view.findViewById(R.id.imgProductOrderDetail);
        txtName = view.findViewById(R.id.txtNameOrderDetail);
        txtDec = view.findViewById(R.id.txtDescriptionOrderDetail);
        txtPrice = view.findViewById(R.id.txtPriceOrderDetail);
        Button button = view.findViewById(R.id.btnOrder);
        productDao = new ProductDao();

        Bundle bundle = getArguments();
        String id = null;
        if (bundle != null){
                 id = bundle.getString("id");
            if (id == null){
                 id = bundle.getString("idHome");
            }
        }
        procutOrderDetailshome(id);



        return view;
    }

    private void procutOrderDetailshome(String productid) {
        productDao.getProduct(productid,new ProductDao.OnProductLoadedListener() {
            @Override
            public void onloaded(Product product) {
                Glide.with(getContext())
                        .load(product.getProductImage())
                        .placeholder(R.drawable.bench)
                        .error(R.drawable.bench)
                        .into(img);
                txtName.setText(product.getProductName());
                txtDec.setText(product.getProductDescription());
                txtPrice.setText(product.getPrice());
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}