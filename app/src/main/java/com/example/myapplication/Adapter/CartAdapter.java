package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        // ⭐ Format tiền Việt Nam
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(item.getPrice()) + " ₫";

        h.txtName.setText(item.getName());
        h.txtPrice.setText(formattedPrice);
        h.txtQuantity.setText(String.valueOf(item.getQuantity()));

        h.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onQuantityChanged();
        });

        h.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onQuantityChanged();
            }
        });

        h.btnDelete.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
            listener.onQuantityChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity;
        ImageButton btnPlus, btnMinus, btnDelete;

        public CartHolder(@NonNull View v) {
            super(v);

            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            txtQuantity = v.findViewById(R.id.txtQuantity);

            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
