package com.example.myapplication.DAO;



import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Model.OrderList;
import com.example.myapplication.Model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrderDao {
    private FirebaseFirestore db;

    public OrderDao() {
       db = FirebaseFirestore.getInstance();
    }

//    String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String UserID = "u1";

    public void getOrderList(OnSuccessListener<ArrayList<OrderList>>listener){
        db.collection("orders")
                .whereEqualTo("userId",UserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<OrderList>list = new ArrayList<>();
                        for (DocumentSnapshot doc: queryDocumentSnapshots){
                            OrderList orderList = doc.toObject(OrderList.class);
                            list.add(orderList);
                        }
                        listener.onSuccess(list);
                    }
                });
    }
}
