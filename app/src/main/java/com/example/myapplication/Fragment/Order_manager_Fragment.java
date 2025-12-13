package com.example.myapplication.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Adapter.Order_manager_adpater;
import com.example.myapplication.Model.OrderList;
import com.example.myapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Order_manager_Fragment extends Fragment {
    Button btnPickDate;
    RecyclerView rcvList;
    Order_manager_adpater adapter;
    ArrayList<OrderList> list;
    FirebaseFirestore db;
    Spinner spFilterBill;
    ArrayList<OrderList> filterList;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_manager_, container, false);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        rcvList = view.findViewById(R.id.rcvBillList);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        spFilterBill = view.findViewById(R.id.spFilterBill);
        list = new ArrayList<>();
        filterList = new ArrayList<>();
        adapter = new Order_manager_adpater(getContext(), filterList);
        rcvList.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAllOrders();

        btnPickDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, day) -> {

                        String selectedDate = String.format(
                                Locale.getDefault(),
                                "%02d/%02d/%d",
                                day, month + 1, year
                        );

                        btnPickDate.setText(selectedDate);
                        filterByDate(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });
        // spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{
                        "Tất cả hóa đơn",
                        "Đã thanh toán",
                        "Đang giao hàng",
                        "Giao hàng thành công",
                        "Đã hủy"
                }
        );
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilterBill.setAdapter(filterAdapter);
        // su ly click
        spFilterBill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (first) {
                    first = false;
                    return;
                }

                String selected = parent.getItemAtPosition(position).toString();

                filterList.clear();

                if (selected.equals("Tất cả hóa đơn")) {
                    filterList.addAll(list);
                } else {
                    for (OrderList order : list) {
                        if (order.getStatus().equals(selected)) {
                            filterList.add(order);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });




        return view;
    }

    // =========================
    // LOAD TOÀN BỘ ORDER
    // =========================
    private void loadAllOrders() {

        db.collection("orders")
                .orderBy("creatAt") // nếu muốn mới → cũ thì dùng descending
                .get()
                .addOnSuccessListener(query -> {

                    list.clear();

                    for (DocumentSnapshot doc : query) {

                        OrderList order= new OrderList(
                                doc.getId(),
                                doc.getString("creatAt"),
                                doc.getString("status"),
                                Math.toIntExact(doc.getLong("total")),
                                doc.getString("userId"),
                                doc.getString("paymentMethod")
                        );
                        list.add(order);
                        Log.d("hi", String.valueOf(list));
                    }
                    filterList.addAll(list);
                    adapter.notifyDataSetChanged();
                });
    }

    // load theo ngay
    private void filterByDate(String date) {

        filterList.clear();

        for (OrderList order : list) {
            if (order.getCreatAt().contains(date)) {
                filterList.add(order);
            }
        }

        adapter.notifyDataSetChanged();
    }

}
