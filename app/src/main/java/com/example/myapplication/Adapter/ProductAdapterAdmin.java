package com.example.myapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.viewholder>{
    private ArrayList<Product>listProduct;
    private Context context;
    public ProductDao productDao;

    public ProductAdapterAdmin( Context context,ArrayList<Product> listProduct, ProductDao productDao) {
        this.listProduct = listProduct;
        this.context = context;
        this.productDao = productDao;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product_admin,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Product product = listProduct.get(position);
        holder.txtProduct.setText(product.getProductName());
        Glide.with(context)
                .load(product.getProductImage())
                .centerCrop()
                .placeholder(R.drawable.bench)
             .into(holder.imgProduct);


        holder.btnChinhsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                View view1 = inflater.inflate(R.layout.item_dialog_update_product,null);
                EditText edtName = view1.findViewById(R.id.edtNameProduct);
                EditText edtPrice = view1.findViewById(R.id.edtPriceProduct);
                EditText edtImage = view1.findViewById(R.id.edtImgProduct);
                EditText edtDec = view1.findViewById(R.id.edtDescriptionProduct);
                edtName.setText(product.getProductName());
                edtPrice.setText(product.getPrice());
                edtImage.setText(product.getProductImage());
                edtDec.setText(product.getProductDescription());
                Button btnUpdate = view1.findViewById(R.id.btnUpdateProduct);
                builder.setView(view1);
                Dialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = product.getProductId();
                        String name = edtName.getText().toString();
                        String price = edtPrice.getText().toString();
                        String image = edtImage.getText().toString();
                        String dec = edtDec.getText().toString();
                        Product product1 = new Product(name,Integer.parseInt(price),image,dec);
                        productDao = new ProductDao();
                        productDao.UpdateProduct(id,product1);
                        loadProduct2();
                        dialog.dismiss();
                    }

                });
                Button btnCancel = view1.findViewById(R.id.btnCancelProduct);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            productDao.DeleteProduct(product.getProductId());
            loadProduct2();
            }
        });
    }

    private void loadProduct2() {
        productDao.getAllproduct(new OnSuccessListener<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                listProduct.clear();
                listProduct.addAll(products);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtProduct;
        Button btnChinhsua, btnXoa;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            btnChinhsua = itemView.findViewById(R.id.btnChinhsua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }
}
