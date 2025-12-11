package com.example.myapplication.DAO;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Model.Product;
import com.example.myapplication.Model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class CategoryDao {

    private FirebaseFirestore db;

    public CategoryDao(){
        db = FirebaseFirestore.getInstance();
    }

    public void getAllcategory(OnSuccessListener<ArrayList<Category>>listener){
        db.collection("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Category>listcategory = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            Category category = doc.toObject(Category.class);
                            listcategory.add(category);
                        }
                        listener.onSuccess(listcategory);
                    }
                });
    }
    public void AddCategory(Category category){
        DocumentReference documentReference = db.collection("categories").document();
        category.setCategoryId(documentReference.getId());
        documentReference.set(category);
    }

    public void UpdateCategory(String categoryID,Category category){
//        db.collection("products").document(productID).set(product);
        category.setCategoryId(categoryID);
        db.collection("categories").document(categoryID).set(category, SetOptions.merge());

    }
//    public void DeleteProduct(String categoryID){
//        db.collection("categories").document(categoryID).delete();
//    }

    public void deleteCategory(String categoryId, DeleteCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 1. Kiểm tra xem Category còn sách không
        db.collection("products")
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            callback.onFailed("Danh mục còn sản phẩm, không thể xoá!");

                        } else {
                            db.collection("categories").document(categoryId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        callback.onSuccess("Xoá danh mục thành công!");
                                    })
                                    .addOnFailureListener(e -> {
                                        callback.onFailed("Lỗi khi xoá danh mục: " + e.getMessage());
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



    public interface DeleteCallback {
        void onSuccess(String message);
        void onFailed(String error);
    }
}
