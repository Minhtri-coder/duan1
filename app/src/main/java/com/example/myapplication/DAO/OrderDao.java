package com.example.myapplication.DAO;



import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Model.OrderList;
import com.example.myapplication.Model.Orderdetails;
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

    public void getOrderDetails(String OrderId,OrderDetailCallback orderDetailCallback){
        db.collection("orderDetails")
                .whereEqualTo("orderID", OrderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot details : queryDocumentSnapshots){
                            String productId = details.getString("productId");
                            Long quantity = details.getLong("quantity");
                            Double price = details.getDouble("price");
                            DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0); // lấy document đầu tiên
                            OrderList orderList = doc.toObject(OrderList.class);
                            orderDetailCallback.onProductLoaded(orderList);
                            db.collection("products").document("productId")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String name = documentSnapshot.getString("productName");
                                            String img = documentSnapshot.getString("productImage");
                                            Double priceDouble = documentSnapshot.getDouble("price");
                                            int price = priceDouble != null ? priceDouble.intValue() : 0;
                                            Long quantityDouble = documentSnapshot.getLong("quantity");
                                            int quantity = quantityDouble != null ? quantityDouble.intValue() : 0;
                                            Orderdetails orderdetails = new Orderdetails(name,price,quantity,img);
                                            orderDetailCallback.onProductLoaded(orderdetails);
                                        }

                                    });
                        }
                    }

                });
    }

    public interface OrderDetailCallback {
        void onProductLoaded(Orderdetails orderdetails);
        void onProductLoaded(OrderList orderList);
    }
}
