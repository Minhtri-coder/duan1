package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.CartManager;
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {

    private ArrayList<Product> listProduct;
    private Context context;
    private OnProductClickListener onProductClickListener;
    private CartManager cartManager;   // ✅ QUẢN LÝ GIỎ HÀNG

    // ✅ Interface click xem chi tiết
    public interface OnProductClickListener {
        void onclickProduct(Product product);
    }

    public ProductAdapter(Context context, ArrayList<Product> listProduct, OnProductClickListener onProductClickListener) {
        this.listProduct = listProduct;
        this.context = context;
        this.onProductClickListener = onProductClickListener;
        this.cartManager = new CartManager(context);
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

        // ✅ TÊN SẢN PHẨM
        holder.txtProduct.setText(product.getProductName());

        // ✅ FORMAT GIÁ (int → đẹp)
        NumberFormat numberFormat = new DecimalFormat("#,###");
        String gia = numberFormat.format(product.getPrice()).replace(",", ".");
        holder.txtPrice.setText(gia + " VNĐ");

        // ✅ LOAD ẢNH
        Glide.with(context)
                .load(product.getProductImage())
                .placeholder(R.drawable.bench)
                .error(R.drawable.bench)
                .into(holder.imgProduct);

        // ✅ CLICK XEM CHI TIẾT
        holder.imgProduct.setOnClickListener(v -> {
            if (onProductClickListener != null) {
                onProductClickListener.onclickProduct(product);
            }
        });

        // ✅ ADD TO CART
        holder.btnAddcart.setOnClickListener(v -> {

            CartItem item = new CartItem(
                    product.getProductName(),
                    product.getPrice(),   // ✅ GIỜ LÀ int — ĐÚNG
                    1,
                    product.getProductImage()
            );

            cartManager.addToCart(item);

            Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    // ================= VIEW HOLDER =================
    public class viewholder extends RecyclerView.ViewHolder {

        ImageView imgProduct, btnAddcart;
        TextView txtProduct, txtPrice;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtPrice = itemView.findViewById(R.id.txtprice);

            // ✅ btnAddcart PHẢI LÀ ImageView TRONG XML
            btnAddcart = itemView.findViewById(R.id.btnAddcart);
        }
    }
}
