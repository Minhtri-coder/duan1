package com.example.myapplication.DAO;

import com.example.myapplication.Model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class ProductDao {

    private FirebaseFirestore db;

    public ProductDao(){
        db = FirebaseFirestore.getInstance();
    }

    public void getAllproduct(OnSuccessListener<ArrayList<Product>>listener){
        db.collection("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Product>listProduct = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Product product = doc.toObject(Product.class);
                            listProduct.add(product);
                        }
                        listener.onSuccess(listProduct);
                    }
                });
    }
    public void Addproduct(Product product){
        DocumentReference documentReference = db.collection("products").document();
        product.setProductId(documentReference.getId());
        documentReference.set(product);
    }

    public void UpdateProduct(String productID,Product product){
//        db.collection("products").document(productID).set(product);
        product.setProductId(productID);
        db.collection("products").document(productID).set(product, SetOptions.merge());

    }
    public void DeleteProduct(String productID){
        db.collection("products").document(productID).delete();
    }
}
