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

    String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    String UserID = "u1";

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



    public void getOrderDetails(String orderID, OrderDetailCallback callback) {

        // 1. Load orderList từ bảng orders
        db.collection("orders")
                .document(orderID)
                .get()
                .addOnSuccessListener(orderSnap -> {

                    OrderList orderList = orderSnap.toObject(OrderList.class);

                    if (orderList == null) {
                        callback.onProductsLoaded(null, new ArrayList<>());
                        return;
                    }

                    // 2. Load orderDetails
                    db.collection("orderDetails")
                            .whereEqualTo("orderID", orderID)
                            .get()
                            .addOnSuccessListener(detailsSnap -> {

                                if (detailsSnap.isEmpty()) {
                                    callback.onProductsLoaded(orderList, new ArrayList<>());
                                    return;
                                }

                                ArrayList<Orderdetails> items = new ArrayList<>();
                                int totalItems = detailsSnap.size();

                                for (DocumentSnapshot d : detailsSnap) {

                                    String productId = d.getString("productId");
                                    Long qty = d.getLong("quantity");
                                    Double price = d.getDouble("price");

                                    db.collection("products")
                                            .document(productId)
                                            .get()
                                            .addOnSuccessListener(productSnap -> {

                                                String name = productSnap.getString("productName");
                                                String img = productSnap.getString("productImage");

                                                Orderdetails item = new Orderdetails(
                                                        name,
                                                        price != null ? price.intValue() : 0,
                                                        qty != null ? qty.intValue() : 1,
                                                        img
                                                );

                                                items.add(item);

                                                // Nếu load xong hết → trả callback chung
                                                if (items.size() == totalItems) {
                                                    callback.onProductsLoaded(orderList, items);
                                                }
                                            });
                                }
                            });
                });
    }



    public interface OrderDetailCallback {

        void onProductsLoaded(OrderList orderInfo,ArrayList<Orderdetails> list);

    }


}







