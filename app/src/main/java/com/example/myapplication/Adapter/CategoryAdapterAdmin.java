package com.example.myapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DAO.CategoryDao;
import com.example.myapplication.Model.Category;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class CategoryAdapterAdmin extends RecyclerView.Adapter<CategoryAdapterAdmin.viewholder>{
    private ArrayList<Category>listCategory;
    private Context context;
    private CategoryDao categoryDao;
    OnProductClickListener listener;
    public CategoryAdapterAdmin(Context context, ArrayList<Category> listCategory) {
        this.listCategory = listCategory;
        this.context = context;
    }

    public CategoryAdapterAdmin(ArrayList<Category> listCategory, Context context, OnProductClickListener listener) {
        this.listCategory = listCategory;
        this.context = context;
        this.listener = listener;
    }

    public CategoryAdapterAdmin( Context context,ArrayList<Category> listCategory, CategoryDao categoryDao) {
        this.listCategory = listCategory;
        this.context = context;
        this.categoryDao = categoryDao;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_category_admin,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Category category = listCategory.get(position);
        holder.tvCateName.setText(category.getCategoryName());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCategory(category);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(category);
            }
        });

    }

    private void deleteCategory(Category category) {
        String idcateogry = category.getCategoryId();
        categoryDao.deleteCategory(idcateogry, new CategoryDao.DeleteCallback() {
            @Override
            public void onSuccess(String message) {
                loadData();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(context, "Không xoá thể loại khi còn sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void editCategory(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_dialog_update_category,null);
        EditText edtName = view.findViewById(R.id.edtNameCategory);
        edtName.setText(category.getCategoryName());
        Button btnAdd = view.findViewById(R.id.btnUpdateCategory);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = category.getCategoryId();
                String name = edtName.getText().toString();
                Category category = new Category(name);
                categoryDao.UpdateCategory(id,category);
                loadData();
                dialog.dismiss();
            }
        });
        Button btnCancel  = view.findViewById(R.id.btnCancelCategory);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void loadData() {
       categoryDao.getAllcategory(new OnSuccessListener<ArrayList<Category>>() {
           @Override
           public void onSuccess(ArrayList<Category> categories) {
               listCategory.clear();
               listCategory.addAll(categories);
               notifyDataSetChanged();
           }
       });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvCateName;
        ImageButton btnEdit, btnDelete;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvCateName= itemView.findViewById(R.id.tvCateName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
    public interface OnProductClickListener {
        void onProductClick(String productName);
    }
}
