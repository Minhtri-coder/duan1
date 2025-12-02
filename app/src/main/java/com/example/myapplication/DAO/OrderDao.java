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
    public void getOrderDetails(String orderId, OrderDetailCallback callback) {
        db.collection("orderDetails")
                .whereEqualTo("orderID", orderId)
                .get()
                .addOnSuccessListener(orderDetailsSnap -> {

                    if (orderDetailsSnap.isEmpty()) {
                        callback.onProductsLoaded(new ArrayList<>());
                        return;
                    }

                    ArrayList<Orderdetails> list = new ArrayList<>();
                    int totalItems = orderDetailsSnap.size();

                    for (DocumentSnapshot detailDoc : orderDetailsSnap.getDocuments()) {

                        String productId = detailDoc.getString("productId");
                        Long qtyOrder = detailDoc.getLong("quantity");
                        Double priceOrder = detailDoc.getDouble("price");

                        // Load thông tin sản phẩm
                        db.collection("products")
                                .document(productId)
                                .get()
                                .addOnSuccessListener(productSnap -> {

                                    String name = productSnap.getString("productName");
                                    String img = productSnap.getString("productImage");

                                    int qty = qtyOrder != null ? qtyOrder.intValue() : 1;
                                    int price = priceOrder != null ? priceOrder.intValue() : 0;

                                    Orderdetails item = new Orderdetails(name, price, qty, img);
                                    list.add(item);

                                    // Khi load xong hết sản phẩm → trả về callback
                                    if (list.size() == totalItems) {
                                        callback.onProductsLoaded(list);
                                    }

                                });
                    }
                });
    }


    public interface OrderDetailCallback {

        void onProductsLoaded(ArrayList<Orderdetails> list);
    }
}







