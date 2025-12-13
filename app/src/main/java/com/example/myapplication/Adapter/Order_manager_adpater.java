package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.OrderList;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Order_manager_adpater extends RecyclerView.Adapter<Order_manager_adpater.viewholder>{
    Context context;
    ArrayList<OrderList> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Order_manager_adpater(Context context, ArrayList<OrderList> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_manage, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        OrderList order = list.get(position);

        holder.txtBillId.setText("Mã đơn: " + order.getOrderID());

        // bill name user

        holder.txtBillCustomer.setText("User: Đang tải...");

        db.collection("users")
                .document(order.getUserId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("name");
                        holder.txtBillCustomer.setText("User: " + userName);
                    } else {
                        holder.txtBillCustomer.setText("User: Không tồn tại");
                    }

                })
                .addOnFailureListener(e -> {
                    holder.txtBillCustomer.setText("User: Lỗi tải");
                });



        holder.txtBillDate.setText("Ngày: " + order.getCreatAt());

        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.txtBillTotal.setText("Tổng: " + format.format(order.getTotal()) + "₫");

        // ==== Spinner trạng thái ====
// ==== Spinner trạng thái ====
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                new String[]{"Đã thanh toán", "Đã hủy", "Đang giao hàng", "Giao hàng thành công"}
        );
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        holder.spStatus.setAdapter(adapterStatus);

        // ❌ QUAN TRỌNG: reset listener trước
                holder.spStatus.setOnItemSelectedListener(null);

        // set trạng thái hiện tại
                int index = adapterStatus.getPosition(order.getStatus());
                if (index >= 0) {
                    holder.spStatus.setSelection(index, false);
                }

        // ✅ Gắn listener SAU KHI setSelection
        holder.spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String newStatus = parent.getItemAtPosition(position).toString();

                // tránh update lại cùng trạng thái
                if (newStatus.equals(order.getStatus())) return;

                db.collection("orders")
                        .document(order.getOrderID())
                        .update("status", newStatus)
                        .addOnSuccessListener(unused -> {
                            order.setStatus(newStatus);
                        })
                        .addOnFailureListener(e -> {
                            // nếu lỗi có thể rollback spinner
                            holder.spStatus.setSelection(adapterStatus.getPosition(order.getStatus()));
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        holder.btnToggleDetail.setOnClickListener(v -> {

            if (holder.layoutDetail == null) return;

            if (holder.layoutDetail.getVisibility() == View.GONE) {
                holder.layoutDetail.setVisibility(View.VISIBLE);
            } else {
                holder.layoutDetail.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        LinearLayout layoutDetail;
        TextView txtBillId, txtBillCustomer, txtBillDate, txtBillTotal;
        Spinner spStatus;
        ImageView btnToggleDetail;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            txtBillId = itemView.findViewById(R.id.txtBillId);
            txtBillCustomer = itemView.findViewById(R.id.txtBillCustomer);
            txtBillDate = itemView.findViewById(R.id.txtBillDate);
            txtBillTotal = itemView.findViewById(R.id.txtBillTotal);
            spStatus = itemView.findViewById(R.id.spStatus);
            btnToggleDetail = itemView.findViewById(R.id.btnToggleDetail);
            layoutDetail = itemView.findViewById(R.id.layoutDetail);
        }
    }


}
