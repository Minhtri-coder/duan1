package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder>{
    private ArrayList<Product>listProduct;
    private Context context;

    public ProductAdapter( Context context,ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Product product = listProduct.get(position);
        holder.txtProduct.setText(product.getProductName());

        Glide.with(context)
                .load(product.getProductImage())
                .placeholder(R.drawable.bench)
                .error(R.drawable.bench)
                .into(holder.imgProduct);
        holder.btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtProduct;
        Button btnProduct;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            btnProduct = itemView.findViewById(R.id.btnProduct);
        }
    }
}
