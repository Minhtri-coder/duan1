package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Adapter.CategoryAdapter;
import com.example.myapplication.Adapter.CategoryAdapterAdmin;
import com.example.myapplication.DAO.CategoryDao;
import com.example.myapplication.Model.Category;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    CategoryDao categoryDao;
    CategoryAdapterAdmin categoryAdapter;
    ArrayList<Category>list;
    FloatingActionButton btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        list = new ArrayList<>();
        categoryDao = new CategoryDao();
        RecyclerView recCate = view.findViewById(R.id.recCat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recCate.setLayoutManager(linearLayoutManager);
        categoryAdapter = new CategoryAdapterAdmin(getContext(),list,categoryDao);
        recCate.setAdapter(categoryAdapter);

        loadData();

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        return view;
    }

    private void addCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_dialog_add_category,null);
        EditText edtName = view.findViewById(R.id.edtNameCategory);
        Button btnAdd = view.findViewById(R.id.btnAddCategory);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                Category category = new Category(name);
                categoryDao.AddCategory(category);
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
                list.clear();
                list.addAll(categories);
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }
}