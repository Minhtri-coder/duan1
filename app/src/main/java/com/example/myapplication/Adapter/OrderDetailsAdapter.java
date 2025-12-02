package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.Orderdetails;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.viewholder>{
    private Context context;
    private ArrayList<Orderdetails>list;

    public OrderDetailsAdapter(Context context, ArrayList<Orderdetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_order_details,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Orderdetails orderdetails = list.get(position);
        holder.txtName.setText(orderdetails.getNameProduct());
        NumberFormat numberFormat = new DecimalFormat("#,###");
        String price = numberFormat.format(orderdetails.getPriceProduct());
        holder.txtPrice.setText(price);
        holder.txtQuantity.setText(String.valueOf(orderdetails.getQuantityProduct()));
        Glide.with(context)
                .load(orderdetails.getImgProduct())
                .placeholder(R.drawable.bench)
                .error(R.drawable.bench)
                .into(holder.imgOrderDetails);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imgOrderDetails;
        TextView txtName, txtPrice, txtQuantity, txtTotal;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgOrderDetails = itemView.findViewById(R.id.imgOrderDetails);
            txtName = itemView.findViewById(R.id.txtNameOrderDetail);
            txtPrice = itemView.findViewById(R.id.txtPriceOrderDetail);
            txtQuantity = itemView.findViewById(R.id.txtQuantityOrderDetails);

        }
    }
}
