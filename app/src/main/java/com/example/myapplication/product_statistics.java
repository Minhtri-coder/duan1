package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class product_statistics extends AppCompatActivity {
    PieChart pieChart;
    TextView txtTotalSales,txtRevenue,txtProductName;
    ImageView imgProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_statistics);
        pieChart = findViewById(R.id.pie_chart);
        txtTotalSales = findViewById(R.id.txtTotalSales);
        txtRevenue = findViewById(R.id.txtRevenue);
        txtProductName = findViewById(R.id.txtProductName);
        imgProduct= findViewById(R.id.imgProduct);
        // goi firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orderDetails")
                .get()
                .addOnSuccessListener(query -> {

                    HashMap<String, Integer> mapSold = new HashMap<>();

                    for (DocumentSnapshot doc : query.getDocuments()) {

                        String productId = doc.getString("productId");
                        Long q = doc.getLong("quantity");
                        int quantity = (q != null) ? q.intValue() : 0;

                        if (productId != null) {
                            mapSold.put(productId,
                                    mapSold.containsKey(productId)
                                            ? mapSold.get(productId) + quantity
                                            : quantity);
                        }
                    }

                    // 🔥 Lấy tên sản phẩm từ collection products
                    FirebaseFirestore.getInstance()
                            .collection("products")
                            .get()
                            .addOnSuccessListener(productsQuery -> {

                                HashMap<String, String> mapName = new HashMap<>();
                                HashMap<String, Long> mapPrice = new HashMap<>();
                                HashMap<String, String> mapImage = new HashMap<>();
                                for (DocumentSnapshot p : productsQuery.getDocuments()) {
                                    String id = p.getString("productId");
                                    String name = p.getString("productName");
                                    Long price = p.getLong("price");
                                    String image = p.getString("productImage");
                                    if (id != null && name != null)
                                        mapName.put(id, name);
                                    if (price != null) mapPrice.put(id, price);
                                    if (image != null) mapImage.put(id, image);
                                }
                                thongKeVaVeBieuDo(mapSold, mapName, mapPrice, mapImage);
                            });
                });
    }
        // ham thong ke
        private void thongKeVaVeBieuDo(HashMap<String, Integer> mapSold,
                                       HashMap<String, String> mapName,
                                       HashMap<String, Long> mapPrice,
                                       HashMap<String, String> mapImage) {

            ArrayList<String> keys = new ArrayList<>(mapSold.keySet());

            Collections.sort(keys, (a, b) -> mapSold.get(b) - mapSold.get(a));

            int top = Math.min(keys.size(), 5);

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            int sold;
            for (int i = 0; i < top; i++) {
                String productId = keys.get(i);
                 sold = mapSold.get(productId);
                // 👉 Lấy tên sản phẩm
                String productName = mapName.get(productId);
                if (productName == null) productName = productId;
                pieEntries.add(new PieEntry(sold,productName));
            }
            // 👉 LẤY BEST SELLER (SẢN PHẨM BÁN NHIỀU NHẤT)
            String bestProductId = keys.get(0);        // ID top 1
            int bestSold = mapSold.get(bestProductId); // số lượng bán nhiều nhất
            txtTotalSales.setText(String.valueOf(bestSold));
            // lay ten best seller
            String bestName= mapName.get(bestProductId);
            txtProductName.setText(bestName);
            // lay gia
            long bestPrice = mapPrice.containsKey(bestProductId) ? mapPrice.get(bestProductId) : 0;
            long totalRevenue = bestSold * bestPrice;
            // dinh dau lai gia

            txtRevenue.setText(String.valueOf(totalRevenue));
            // laay hinh anh
            String bestImage = mapImage.get(bestProductId);
            // set anh
            Glide.with(this)
                    .load(bestImage)
                    .into(imgProduct);

            vePieChart(pieEntries);
        }

    // ham ve bieu do
    private void vePieChart(ArrayList<PieEntry> pieEntries) {

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Top sản phẩm bán chạy");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setSliceSpace(3f);  // khoảng cách giữa các miếng
        pieDataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.setUsePercentValues(true); // hiển thị %
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(35f);
        pieChart.setTransparentCircleRadius(45f);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateXY(1500, 1500);

        pieChart.invalidate();
    }
}