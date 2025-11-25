package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter_home extends RecyclerView.Adapter<ProductAdapter_home.viewholder>{
    private ArrayList<Product>listProduct;
    private Context context;
    public OnProductClickListener onProductClickListener;
    public interface OnProductClickListener{
        void onclickProduct(Product product);
    }
//    public ProductAdapter( Context context,ArrayList<Product> listProduct,OnProductClickListener) {
//        this.listProduct = listProduct;
//        this.context = context;
//    }


    public ProductAdapter_home(Context context, ArrayList<Product> listProduct, OnProductClickListener onProductClickListener) {
        this.listProduct = listProduct;
        this.context = context;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product_main,parent,false);
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
//        holder.btnProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        NumberFormat numberFormat = new DecimalFormat("#,###");
        String price = numberFormat.format(product.getPrice());
        price = price.replace(",", ".");
        holder.txtPrice.setText(price);
        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductClickListener.onclickProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtProduct, txtPrice;
        Button btnProduct;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProduct = itemView.findViewById(R.id.txtProduct);
//            btnProduct = itemView.findViewById(R.id.btnProduct);
            txtPrice = itemView.findViewById(R.id.txtprice);
        }
    }
}
