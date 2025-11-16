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

import com.example.myapplication.Model.item_category;
import com.example.myapplication.R;


import java.util.ArrayList;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder>{
    private ArrayList<item_category>listCategory;
    private Context context;

    public CategoryAdapter( Context context,ArrayList<item_category> listCategory) {
        this.listCategory = listCategory;
        this.context = context;
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
        item_category product = listCategory.get(position);
        holder.txtcate.setText(product.getName());
        Glide.with(context)
                .load(product.getImg())
                .centerCrop()
                .placeholder(R.drawable.bench)
                .into(holder.imgcate);

    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imgcate;
        TextView txtcate;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgcate = itemView.findViewById(R.id.img_cate);
            txtcate= itemView.findViewById(R.id.txtcate);
        }
    }
}
