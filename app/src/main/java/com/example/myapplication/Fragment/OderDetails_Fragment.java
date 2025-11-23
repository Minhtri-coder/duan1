package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.Source;


public class OderDetails_Fragment extends Fragment {
    ImageView img;
    TextView txtName,txtDec,txtPrice;
    ProductDao productDao;
    Product product;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oder_details_, container, false);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = view.getContext()
                        .getSharedPreferences("PRODUCT_DATA", Context.MODE_PRIVATE);
                String cartJson = preferences.getString("cart_list", "[]");
                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("img", product.getProductImage());
//                editor.putString("name", product.getProductName());
//                editor.putString("description", product.getProductDescription());
//                editor.putString("price", product.getPrice());
                JSONArray array = null;
                try {
                    array = new JSONArray(cartJson);
                    JSONObject obj = new JSONObject();
                    obj.put("name", product.getProductName());
                    obj.put("price", product.getPrice());
                    obj.put("img", product.getProductImage());

                    array.put(obj); // thêm sản phẩm mới
                    editor.putString("cart_list", array.toString());
                    editor.apply();
                    Toast.makeText(view.getContext(), "Add thanh cong r bro", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("loi ne", "Lỗi xảy ra: ", e);
                }
            }
        });
        return view;
    }

    private void procutOrderDetailshome(String productid) {
        productDao.getProduct(productid, new ProductDao.OnProductLoadedListener() {
            @Override
            public void onloaded(Product loadedProduct) {
                // Gán biến toàn cục
                product = loadedProduct;

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
                Toast.makeText(getContext(), "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }






}