package com.example.myapplication.DAO;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.myapplication.Adapter.ProductAdapter;
import com.example.myapplication.Model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public void getProduct(String id,OnProductLoadedListener productLoadedListener){
       db.collection("products").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
            Product product = documentSnapshot.toObject(Product.class);
            if (product != null){
                productLoadedListener.onloaded(product);
            }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
            productLoadedListener.onError(e);
           }
       });
    }

    public interface OnProductLoadedListener{
        void onloaded(Product product);
        void onError(Exception e);
    }
}
