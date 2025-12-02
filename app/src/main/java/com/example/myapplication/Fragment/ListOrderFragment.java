package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.OrderListAdapter;
import com.example.myapplication.DAO.OrderDao;
import com.example.myapplication.Model.OrderList;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


public class ListOrderFragment extends Fragment {
    private OrderDao orderDao;
    private ArrayList<OrderList>list;
    private OrderListAdapter orderListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_order, container, false);
        RecyclerView recListOrder = view.findViewById(R.id.recListOrder);
        list = new ArrayList<>();
        orderDao = new OrderDao();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recListOrder.setLayoutManager(linearLayoutManager);
        orderListAdapter = new OrderListAdapter(getContext(),list);
        recListOrder.setAdapter(orderListAdapter);
        loadData();
        return view;
    }

    private void loadData() {
        orderDao.getOrderList(new OnSuccessListener<ArrayList<OrderList>>() {
            @Override
            public void onSuccess(ArrayList<OrderList> orderLists) {
                list.clear();
                list.addAll(orderLists);
                orderListAdapter.notifyDataSetChanged();
            }
        });

    }
}