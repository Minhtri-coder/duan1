package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private MaterialButton btnLogin;
    private TextView txtSignup;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);

        // Đăng nhập
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Chuyển sang Register
        txtSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void attemptLogin() {

        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng nhập Firebase Auth
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(auth -> {

                    // Lấy role trong Firestore
                    db.collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(snapshot -> {

                                String role = snapshot.getDocuments().get(0).getString("role");

                                if ("admin".equals(role)) {
                                    Toast.makeText(this, "Chào Admin!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, AdminMain.class));

                                } else {
                                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                }

                                finish();
                            });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show());
    }
}
