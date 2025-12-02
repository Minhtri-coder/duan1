package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    Context context;
    ArrayList<CartItem> list;
    CartListener listener;

    public interface CartListener {
        void onQuantityChanged();
    }

    public CartAdapter(Context context, ArrayList<CartItem> list, CartListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder h, int position) {
        CartItem item = list.get(position);

        // ✅ FORMAT GIÁ VNĐ
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(item.getPrice()) + " ₫";

        h.txtName.setText(item.getName());
        h.txtPrice.setText(formattedPrice);
        h.txtQuantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.bench)
                .error(R.drawable.bench)
                .into(h.imgProduct);

        // ✅ LOAD ẢNH SẢN PHẨM (FIX LỖI KHÔNG HIỂN THỊ ẢNH)
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.bench) // ảnh mặc định
                .error(R.drawable.bench)
                .into(h.imgProduct);

        // ✅ NÚT +
        h.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(h.getAdapterPosition());
            listener.onQuantityChanged();
        });

        // ✅ NÚT -
        h.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(h.getAdapterPosition());
                listener.onQuantityChanged();
            }
        });

        // ✅ NÚT XOÁ (FIX CRASH)
        h.btnDelete.setOnClickListener(v -> {
            int pos = h.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, list.size());
                listener.onQuantityChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice, txtQuantity;
        ImageButton btnPlus, btnMinus, btnDelete;
 
        ImageView imgProduct; // ✅ ẢNH SẢN PHẨM

//        ImageView imgProduct;


        public CartHolder(@NonNull View v) {
            super(v);

            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            txtQuantity = v.findViewById(R.id.txtQuantity);
            imgProduct = v.findViewById(R.id.imgProduct);

            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
            btnDelete = v.findViewById(R.id.btnDelete);

            imgProduct = v.findViewById(R.id.imgProduct); // ✅ BẮT ẢNH
        }
    }
}
