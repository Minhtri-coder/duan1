package com.example.myapplication.Admin;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Custom_statistics_Fragment extends Fragment {

    TextView txttotalRevenue, txttotal_sale, txttotal_customer, txttotal_order;
    BarChart bar_chart;
    Button btnPickDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_custom_statistics_, container, false);

        txttotalRevenue = view.findViewById(R.id.txttotalRevenue);
        txttotal_sale = view.findViewById(R.id.txttotal_sale);
        txttotal_customer = view.findViewById(R.id.txttotal_customer);
        txttotal_order = view.findViewById(R.id.txttotal_order);
        bar_chart = view.findViewById(R.id.bar_chart);
        btnPickDate = view.findViewById(R.id.btnPickDate);

        btnPickDate.setOnClickListener(v -> openDatePicker());

        loadStatistics(getFullMonthDays(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH)));

        return view;
    }

    // ===========================
    // CHỌN NGÀY → LẤY CẢ THÁNG
    // ===========================
    private void openDatePicker() {
        Calendar cal = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {

                    List<String> listDays = getFullMonthDays(year, month);

                    Log.d("DATE_PICK", "Chọn ngày: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    Log.d("DATE_PICK", "Load tháng đầy đủ: " + listDays.size() + " ngày");

                    loadStatistics(listDays);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    // ============================
    // LẤY TOÀN BỘ NGÀY TRONG THÁNG
    // ============================
    private List<String> getFullMonthDays(int year, int month) {
        List<String> days = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= maxDay; i++) {
            days.add(String.format("%02d/%02d/%d", i, month + 1, year));
        }

        return days;
    }

    // ===============================
    // LOAD THỐNG KÊ THEO THÁNG
    // ===============================
    private void loadStatistics(List<String> listDates) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders").get().addOnSuccessListener(orderQuery -> {

            int totalOrder = orderQuery.size();
            txttotal_order.setText(String.valueOf(totalOrder));

            long totalRevenue = 0;
            HashMap<String, Boolean> mapCustomer = new HashMap<>();
            Map<String, String> orderDateMap = new HashMap<>();

            for (DocumentSnapshot o : orderQuery) {

                Long price = o.getLong("total");
                if (price != null) totalRevenue += price;

                String uid = o.getString("userId");
                if (uid != null) mapCustomer.put(uid, true);

                String createdAt = o.getString("creatAt");
                String orderId = o.getId();

                if (createdAt != null) {
                    String pureDate = createdAt.split(" ")[0];
                    orderDateMap.put(orderId, pureDate);
                }
            }

            txttotal_customer.setText(String.valueOf(mapCustomer.size()));

            DecimalFormat formatter = new DecimalFormat("#,###");
            txttotalRevenue.setText(formatter.format(totalRevenue));

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
                        String date = orderDateMap.get(orderId);

                        if (date != null && listDates.contains(date)) {
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

    // ================================
    //  VẼ BIỂU ĐỒ BARCHART
    // ================================
    private void veBarChart(TreeMap<String, Long> dailyRevenue) {

        if (dailyRevenue.isEmpty()) {
            bar_chart.clear();
            bar_chart.invalidate();
            return;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;

        for (String date : dailyRevenue.keySet()) {

            entries.add(new BarEntry(index, dailyRevenue.get(date).floatValue()));

            // CHỈ LẤY dd/MM
            String shortLabel = date.substring(0, 5);
            labels.add(shortLabel);

            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColor(Color.parseColor("#D8A86B"));
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        bar_chart.setData(data);

        XAxis xAxis = bar_chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);

        bar_chart.getAxisRight().setEnabled(false);
        bar_chart.setFitBars(true);
        bar_chart.getDescription().setEnabled(false);
        bar_chart.setVisibleXRangeMaximum(100);
        bar_chart.animateY(1200);
        bar_chart.invalidate();
    }
}
