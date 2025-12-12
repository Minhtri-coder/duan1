package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Category;
import com.example.myapplication.R;


import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder>{
    private ArrayList<Category>listCategory;
    private Context context;
    OnProductClickListener listener;
    public CategoryAdapter( Context context,ArrayList<Category> listCategory) {
        this.listCategory = listCategory;
        this.context = context;
    }

    public CategoryAdapter(ArrayList<Category> listCategory, Context context, OnProductClickListener listener) {
        this.listCategory = listCategory;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_category,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Category product = listCategory.get(position);
        holder.txtcate.setText(product.getCategoryName());
        // set on click
        int posion = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "vi tri"+posion+"ten"+product.getName(), Toast.LENGTH_SHORT).show();
                if(posion !=RecyclerView.NO_POSITION){
                    listener.onProductClick(product.getCategoryName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView txtcate;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            txtcate= itemView.findViewById(R.id.txtcate);
        }
    }
    public interface OnProductClickListener {
        void onProductClick(String productName);
    }
}
