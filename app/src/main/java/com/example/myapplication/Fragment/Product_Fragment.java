package com.example.myapplication.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.ProductAdapter;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;

import java.util.ArrayList;

public class Product_Fragment extends Fragment {
    private RecyclerView recProduct;
    private ArrayList<Product> listProduct;
    private ProductDao productDao;
    private ProductAdapter productAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo ProductDao ở đây, trước khi gọi bất kỳ hàm nào
        productDao = new ProductDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_, container, false);

        // RecyclerView
        recProduct = view.findViewById(R.id.recProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recProduct.setLayoutManager(gridLayoutManager);

        listProduct = new ArrayList<>();

        // Adapter
        productAdapter = new ProductAdapter(getContext(), listProduct, product -> {
            Fragment orderFragment = new ProductDetails_Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", product.getProductId());
            orderFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framecontent, orderFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recProduct.setAdapter(productAdapter);

        // Lấy query từ MainActivity nếu có
        Bundle bundle = getArguments();
        String searchQuery = "";
        if (bundle != null) {
            searchQuery = bundle.getString("searchQuery", "");
        }

        // Nếu có query → search, nếu rỗng → load tất cả
        if (searchQuery.isEmpty()) {
            loadProducts();
        } else {
            searchProducts(searchQuery);
        }

        return view;
    }

    // Load tất cả sản phẩm
    private void loadProducts() {
        productDao.getAllproduct(products -> {
            listProduct.clear();
            listProduct.addAll(products);
            productAdapter.notifyDataSetChanged();
        });
    }

    // Search sản phẩm theo query
    public void searchProducts(String query) {
        if (query == null || query.isEmpty()) {
            loadProducts();
            return;
        }

        productDao.getAllproduct(products -> {
            listProduct.clear();
            for (Product p : products) {
                if (p.getProductName().toLowerCase().contains(query.toLowerCase())) {
                    listProduct.add(p);
                }
            }
            productAdapter.notifyDataSetChanged();
        });
    }
}
