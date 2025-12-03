package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Adapter.OrderDetailsAdapter;
import com.example.myapplication.DAO.OrderDao;
import com.example.myapplication.Model.OrderList;
import com.example.myapplication.Model.Orderdetails;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class Order_details_Fragment extends Fragment {

    private OrderDao orderDao;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Orderdetails> list;
    TextView txtOrderId, txtDate ,txtTotal;
    private OrderDetailsAdapter orderDetailsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details_, container, false);
        txtOrderId = view.findViewById(R.id.txtOrderNumber);
        txtDate = view.findViewById(R.id.txtOrderDate);
        txtTotal = view.findViewById(R.id.txtTotalAmount);
        RecyclerView recOrderDetails = view.findViewById(R.id.recOrderDetails);
        orderDao = new OrderDao();
        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recOrderDetails.setLayoutManager(linearLayoutManager);
        orderDetailsAdapter = new OrderDetailsAdapter(getContext(),list);
        recOrderDetails.setAdapter(orderDetailsAdapter);
        loadData();
        return view;

    }

    private void loadData() {
        Bundle bundle = getArguments();
        String orderid = "";
        if (bundle != null) {
            orderid = bundle.getString("orderid");
        }
      orderDao.getOrderDetails(orderid, new OrderDao.OrderDetailCallback() {
          @Override
          public void onProductsLoaded(OrderList orderInfo, ArrayList<Orderdetails> list2) {
              txtOrderId.setText(orderInfo.getOrderID());
              txtDate.setText(orderInfo.getCreatAt());
              NumberFormat numberFormat = new DecimalFormat("#,###");
              String price = numberFormat.format(orderInfo.getTotal());
              price = price.replace(",",".");
              txtTotal.setText(price  + " đ");
              list.clear();
              list.addAll(list2);
              orderDetailsAdapter.notifyDataSetChanged();
          }
      });

    }
}