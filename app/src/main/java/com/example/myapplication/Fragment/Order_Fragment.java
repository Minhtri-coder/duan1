package com.example.myapplication.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class Order_Fragment extends Fragment {
    RecyclerView rcvProduct;
    TextView txtTotal_product,txtTotal_Ship,txtTotalFull;
    Button btnBuy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_, container, false);
        // Inflate the layout for this fragment
        rcvProduct= view.findViewById(R.id.rcvProduct);
        txtTotal_product= view.findViewById(R.id.txtTotal_product);
        txtTotal_Ship= view.findViewById(R.id.txtTotal_Ship);
        txtTotalFull= view.findViewById(R.id.txtTotalFull);


        return view;

    }

}