package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.myapplication.CartManager;
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {

    private ArrayList<Product> listProduct;
    private Context context;
    private OnProductClickListener onProductClickListener;
    private CartManager cartManager;

    public interface OnProductClickListener {
        void onclickProduct(Product product);
    }

    public ProductAdapter(Context context, ArrayList<Product> listProduct, OnProductClickListener onProductClickListener) {
        this.listProduct = listProduct;
        this.context = context;
        this.onProductClickListener = onProductClickListener;

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        this.cartManager = new CartManager(context, userId);
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Product product = listProduct.get(position);

        holder.txtProduct.setText(product.getProductName());

        NumberFormat numberFormat = new DecimalFormat("#,###");
        String gia = numberFormat.format(product.getPrice()).replace(",", ".");
        holder.txtPrice.setText(gia + " VNĐ");

        Glide.with(context)
                .load(product.getProductImage())
                .placeholder(R.drawable.bench)
                .error(R.drawable.bench)
                .into(holder.imgProduct);

        holder.imgProduct.setOnClickListener(v -> {
            if (onProductClickListener != null) {
                onProductClickListener.onclickProduct(product);
            }
        });

        holder.btnAddcart.setOnClickListener(v -> {
//            if (onProductClickListener != null){
//                onProductClickListener.onclickProduct(product);
//            }
            CartItem item = new CartItem(
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    1,
                    product.getProductImage()
            );

            cartManager.addToCart(item);

            // ✅ GỬI TÍN HIỆU CẬP NHẬT BADGE NGAY
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(new Intent("UPDATE_BADGE"));

            Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        ImageView imgProduct, btnAddcart;
        TextView txtProduct, txtPrice;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtPrice = itemView.findViewById(R.id.txtprice);
            btnAddcart = itemView.findViewById(R.id.btnAddcart);
        }
    }
}
