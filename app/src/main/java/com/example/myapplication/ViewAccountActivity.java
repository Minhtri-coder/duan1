package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Button;



public class ViewAccountActivity extends AppCompatActivity {

    TextView txtEmail, txtPhone, txtAddress;
    ImageButton btnBack;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);
        btnBack = findViewById(R.id.btn_back_info);

        btnBack.setOnClickListener(v -> finish());
        Button btnEdit;
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        btnEdit = findViewById(R.id.btnSave);

        btnEdit.setOnClickListener(v -> {
            startActivity(new Intent(ViewAccountActivity.this, UpdateAccountActivity.class));
        });
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            txtEmail.setText("Email: " + user.getEmail());

            db.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(doc -> {
                        txtPhone.setText("Số điện thoại: " + doc.getString("phone"));
                        txtAddress.setText("Địa chỉ: " + doc.getString("address"));
                    });
        }
    }
}
