package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateAccountActivity extends AppCompatActivity {

    EditText edtEmail, edtPhone, edtAddress;
    Button btnSave;
    ImageButton btnBack;


    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        btnBack = findViewById(R.id.btn_back_update);

        btnBack.setOnClickListener(v -> finish());

        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnSave = findViewById(R.id.btnSave);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Load thông tin cũ
        if (auth.getCurrentUser() == null) return;
        String uid = auth.getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    edtEmail.setText(doc.getString("email"));
                    edtPhone.setText(doc.getString("phone"));
                    edtAddress.setText(doc.getString("address"));
                });

        btnSave.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (auth.getCurrentUser() == null) return;

            // ✅ Update EMAIL trong Firebase Authentication
            auth.getCurrentUser().updateEmail(email).addOnSuccessListener(unused -> {

                // ✅ Sau đó update Firestore
                db.collection("users").document(uid)
                        .update(
                                "email", email,
                                "phone", phone,
                                "address", address
                        )
                        .addOnSuccessListener(unused1 -> {
                            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );

            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Lỗi cập nhật email: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        });
    }
}
