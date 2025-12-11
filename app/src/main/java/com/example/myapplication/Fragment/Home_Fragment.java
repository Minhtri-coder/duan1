package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapter.CategoryAdapter;
import com.example.myapplication.Adapter.ProductAdapter_home;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.Model.Product;
import com.example.myapplication.Model.Category;
import com.example.myapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Home_Fragment extends Fragment {
    RecyclerView recCategory, recProduct;
    ArrayList<Category> ListCate;
    private ArrayList<Product> listProduct;
    private ProductDao productDao;
    private ProductAdapter_home productAdapter;
    CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        recCategory = view.findViewById(R.id.recCategory);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 5);
        recCategory.setLayoutManager(gridLayoutManager);
        ListCate = new ArrayList<>();
        ListCate.add(new Category( "Sofa"));
        ListCate.add(new Category( "Ghế"));
        ListCate.add(new Category("Giường"));
        ListCate.add(new Category("Bàn"));
        ListCate.add(new Category("Đèn"));
        categoryAdapter = new CategoryAdapter(requireContext(), ListCate);
        categoryAdapter = new CategoryAdapter(ListCate, requireContext(), categoryName -> {
                    filterCategoryFromFirebase(categoryName);
                });
        recCategory.setAdapter(categoryAdapter);
        // product
        View view2 = inflater.inflate(R.layout.fragment_product_, container, false);
        recProduct = view.findViewById(R.id.recProduct);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        recProduct.setLayoutManager(gridLayoutManager2);
        listProduct = new ArrayList<>();
        productAdapter = new ProductAdapter_home(getContext(), listProduct, new ProductAdapter_home.OnProductClickListener() {
            @Override
            public void onclickProduct(Product product) {
                Fragment OrderFragment = new ProductDetails_Fragment();
                Bundle bundleHome = new Bundle();
                bundleHome.putString("idHome",product.getProductId());
                OrderFragment.setArguments(bundleHome);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framecontent,OrderFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        recProduct.setAdapter(productAdapter);
        productDao = new ProductDao();
        loadProducts();
        return view;
    }

    private void loadProducts() {
        productDao.getAllproduct(products -> {
            listProduct.clear();
            listProduct.addAll(products);
            productAdapter.notifyDataSetChanged();
        });
    }
    // loc san pham theo item
    private void filterCategoryFromFirebase(String categoryName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .whereGreaterThanOrEqualTo("productName", categoryName)
                .whereLessThanOrEqualTo("productName", categoryName + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    listProduct.clear(); // danh sách sản phẩm hiển thị

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Product p = doc.toObject(Product.class);
                        listProduct.add(p);
                    }

                    productAdapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Đã lọc theo: " + categoryName, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}