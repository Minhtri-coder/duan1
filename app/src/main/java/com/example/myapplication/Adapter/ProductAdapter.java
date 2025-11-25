package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {

    private ArrayList<Product> listProduct;
    private Context context;
    private OnProductClickListener onProductClickListener;
    private CartManager cartManager;  // ⭐ QUAN TRỌNG

    public interface OnProductClickListener {
        void onclickProduct(Product product);
    }

    public ProductAdapter(Context context, ArrayList<Product> listProduct, OnProductClickListener onProductClickListener) {
        this.listProduct = listProduct;
        this.context = context;
        this.onProductClickListener = onProductClickListener;
        cartManager = new CartManager(context);   // ⭐ Khởi tạo CartManager
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
        holder.txtPrice.setText(product.getPrice());

        Glide.with(context)
                .load(product.getProductImage())
                .placeholder(R.drawable.bench)
                .error(R.drawable.bench)
                .into(holder.imgProduct);

        // ⭐ Bấm vào ảnh → xem chi tiết
        holder.imgProduct.setOnClickListener(v -> onProductClickListener.onclickProduct(product));

        // ⭐ Nút Add to Cart
        holder.btnAddcart.setOnClickListener(v -> {
            try {
                double price = Double.parseDouble(product.getPrice().replace(",", ""));

                CartItem item = new CartItem(
                        product.getProductName(),
                        price,
                        1,
                        product.getProductImage()
                );

                cartManager.addToCart(item);

                Toast.makeText(context, "Đã thêm " + product.getProductName() + " vào giỏ!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Lỗi giá sản phẩm!", Toast.LENGTH_SHORT).show();
            }
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

            // ⭐ Nút Add to Cart trong item_product.xml
            btnAddcart = itemView.findViewById(R.id.btnAddcart);
        }
    }
}
