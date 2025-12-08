package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class custom_statistics extends AppCompatActivity {

    TextView txttotalRevenue, txttotal_sale, txttotal_customer, txttotal_order;
    BarChart bar_chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_statistics);

        txttotalRevenue = findViewById(R.id.txttotalRevenue);
        txttotal_sale = findViewById(R.id.txttotal_sale);
        txttotal_customer = findViewById(R.id.txttotal_customer);
        txttotal_order = findViewById(R.id.txttotal_order);
        bar_chart = findViewById(R.id.bar_chart);

        loadStatistics();
    }

    private void loadStatistics() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // ================================
        // 1) Lấy tổng đơn, tổng doanh thu, tổng khách
        // ================================
        db.collection("orders").get().addOnSuccessListener(orderQuery -> {

            int totalOrder = orderQuery.size();
            txttotal_order.setText(String.valueOf(totalOrder));

            long totalRevenue = 0;
            HashMap<String, Boolean> mapCustomer = new HashMap<>();
            Map<String, String> orderDateMap = new HashMap<>(); // Lưu ngày của từng order

            for (DocumentSnapshot o : orderQuery) {
                Long price = o.getLong("total");
                if (price != null) totalRevenue += price;

                String uid = o.getString("userId");
                if (uid != null) mapCustomer.put(uid, true);

                String orderId = o.getId();
                String createdAt = o.getString("creatAt");
                if (createdAt != null) {
                    orderDateMap.put(orderId, createdAt.split(" ")[0]); // dd/MM/yyyy
                }
            }

            txttotal_customer.setText(String.valueOf(mapCustomer.size()));
            DecimalFormat formatter = new DecimalFormat("#,###");
            txttotalRevenue.setText(formatter.format(totalRevenue));

            // ================================
            // 2) Lấy total sales + doanh thu theo ngày từ orderDetails
            // ================================
            db.collection("orderDetails").get().addOnSuccessListener(detailQuery -> {
                TreeMap<String, Long> dailyRevenue = new TreeMap<>();
                int totalSale = 0;

                for (DocumentSnapshot d : detailQuery) {
                    Long quantity = d.getLong("quantity");
                    Long price = d.getLong("price");
                    String orderId = d.getString("orderID");

                    if (quantity != null) totalSale += quantity;

                    if (price != null && quantity != null && orderId != null) {
                        long money = price * quantity;
                        String date = orderDateMap.get(orderId); // lấy ngày từ orders

                        if (date != null) {
                            long old = dailyRevenue.containsKey(date) ? dailyRevenue.get(date) : 0;
                            dailyRevenue.put(date, old + money);
                        }
                    }
                }

                txttotal_sale.setText(String.valueOf(totalSale));
                veBarChart(dailyRevenue);
            });
        });
    }

    // ================================================
    // Vẽ BarChart theo ngày
    // ================================================
    private void veBarChart(TreeMap<String, Long> dailyRevenue) {
        if (dailyRevenue.isEmpty()) return;

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        for (String date : dailyRevenue.keySet()) {

            // Giá trị Bar
            entries.add(new BarEntry(index, dailyRevenue.get(date).floatValue()));

            // Chuyển dd/MM/yyyy → MM/dd
            String[] parts = date.split("/");
            if (parts.length == 3) {
                String dd = parts[0];
                String MM = parts[1];
                labels.add(MM + "/" + dd);  // Hiển thị tháng/ngày
            } else {
                labels.add(date);
            }

            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo ngày");
        dataSet.setColor(Color.parseColor("#D8A86B"));
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        bar_chart.setData(data);

        XAxis xAxis = bar_chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(Math.min(labels.size(), 6), true);
        xAxis.setTextSize(12f);

        bar_chart.getAxisRight().setEnabled(false);
        bar_chart.setVisibleXRangeMaximum(6);
        bar_chart.setFitBars(true);
        bar_chart.getDescription().setEnabled(false);
        bar_chart.animateY(1200);
        bar_chart.invalidate();
    }
}
