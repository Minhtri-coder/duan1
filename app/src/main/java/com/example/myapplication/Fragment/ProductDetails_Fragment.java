package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.CartManager;
import com.example.myapplication.DAO.ProductDao;
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ProductDetails_Fragment extends Fragment {

    ImageView img;
    TextView txtName, txtDec, txtPrice, qtyMinus, qtyPlus, qtyDisplay;
    Button addToCartButton;

    ProductDao productDao;

    private Product currentProduct;   // ✅ CHỈ KHAI BÁO 1 LẦN DUY NHẤT
    int quantity = 1;                 // ✅ SỐ LƯỢNG BAN ĐẦU

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_details_, container, false);

        img = view.findViewById(R.id.imgProductOrderDetail);
        txtName = view.findViewById(R.id.txtNameOrderDetail);
        txtDec = view.findViewById(R.id.txtDescriptionOrderDetail);
        txtPrice = view.findViewById(R.id.txtPriceOrderDetail);

        qtyMinus = view.findViewById(R.id.qtyMinus);
        qtyPlus = view.findViewById(R.id.qtyPlus);
        qtyDisplay = view.findViewById(R.id.qtyDisplay);

        addToCartButton = view.findViewById(R.id.addToCartButton);

        productDao = new ProductDao();

        // ✅ LẤY ID SẢN PHẨM TỪ BUNDLE
        Bundle bundle = getArguments();
        String id = null;
        if (bundle != null) {
            id = bundle.getString("id");
            if (id == null) {
                id = bundle.getString("idHome");
            }
        }

        loadProductDetail(id);
        setupQuantityControl();
        setupAddToCart();

        return view;
    }

    // ✅ LOAD CHI TIẾT SẢN PHẨM
    private void loadProductDetail(String productid) {
        productDao.getProduct(productid, new ProductDao.OnProductLoadedListener() {
            @Override
            public void onloaded(Product product) {

                currentProduct = product;   // ✅ LƯU SẢN PHẨM ĐANG XEM

                Glide.with(requireContext())
                        .load(product.getProductImage())
                        .placeholder(R.drawable.bench)
                        .error(R.drawable.bench)
                        .into(img);

                txtName.setText(product.getProductName());
                txtDec.setText(product.getProductDescription());

                NumberFormat numberFormat = new DecimalFormat("#,###");
                String price = numberFormat.format(product.getPrice()).replace(",", ".");
                txtPrice.setText(price + " ₫");
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Lỗi tải sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ XỬ LÝ NÚT + / -
    private void setupQuantityControl() {

        qtyDisplay.setText(String.valueOf(quantity));

        qtyPlus.setOnClickListener(v -> {
            quantity++;
            qtyDisplay.setText(String.valueOf(quantity));
        });

        qtyMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                qtyDisplay.setText(String.valueOf(quantity));
            }
        });
    }

    // ✅ XỬ LÝ ADD TO CART
    private void setupAddToCart() {

        addToCartButton.setOnClickListener(v -> {

            if (currentProduct == null) {
                Toast.makeText(getContext(), "Sản phẩm chưa sẵn sàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            CartItem cartItem = new CartItem(
                    currentProduct.getProductId(),     // ✅ ĐÚNG CHUẨN ID
                    currentProduct.getProductName(),
                    currentProduct.getPrice(),
                    quantity,
                    currentProduct.getProductImage()
            );

            String userId = requireContext()
                    .getSharedPreferences("USER", Context.MODE_PRIVATE)
                    .getString("userId", "guest");

            CartManager cartManager = new CartManager(requireContext(), userId);


            LocalBroadcastManager.getInstance(requireContext())
                    .sendBroadcast(new Intent("UPDATE_BADGE"));

            cartManager.addToCart(cartItem);

            // ✅ GỬI BROADCAST UPDATE BADGE
            LocalBroadcastManager.getInstance(requireContext())
                    .sendBroadcast(new Intent("UPDATE_BADGE"));

            Toast.makeText(getContext(), "✅ Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
    }
}
