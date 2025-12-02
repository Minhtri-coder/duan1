package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.OrderList;

import com.example.myapplication.Model.Orderdetails;

import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.viewholder>{
    private Context context;
    private ArrayList<OrderList>list;



    public OrderListAdapter(Context context, ArrayList<OrderList> list) {
        this.context = context;
        this.list = list;

    private OnOrderClickListener clickListener;

    public interface OnOrderClickListener{
        void onclickOrder(OrderList orderList);
    }

    public OrderListAdapter(Context context, ArrayList<OrderList> list, OnOrderClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list_order,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        OrderList orderList = list.get(position);
     holder.txtOrderId.setText(orderList.getOrderID());
     holder.txtCreatAt.setText(orderList.getCreatAt());
     holder.txtStatus.setText(orderList.getStatus());
        NumberFormat numberFormat = new DecimalFormat("#,###");
        String price = numberFormat.format(orderList.getTotal());
        price = price.replace(",",".");
        holder.txtTotal.setText(price);
        holder.txtOrderDeatails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickListener != null){
                    clickListener.onclickOrder(orderList);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtCreatAt, txtTotal, txtStatus, txtOrderDeatails;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderID);
            txtCreatAt = itemView.findViewById(R.id.txtCreatAt);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtOrderDeatails = itemView.findViewById(R.id.txtViewDetails);
            txtTotal = itemView.findViewById(R.id.txtTotalPrice);
        }
    }
}
