package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtTaiKhoan, edtEmail, edtSDT, edtMatKhau;
    private MaterialButton btnDangKy;
    private TextView btnTroVe;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnTroVe = findViewById(R.id.btnTroVe);

        // Đăng ký
        btnDangKy.setOnClickListener(v -> attemptRegistration());

        // Trở về Login
        btnTroVe.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {

        // Lấy dữ liệu người dùng nhập
        String taiKhoan = edtTaiKhoan.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSDT.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        // Kiểm tra
        if (taiKhoan.isEmpty() || email.isEmpty() || sdt.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (matKhau.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo tài khoản Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, matKhau)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserToFirestore(user, taiKhoan, sdt, matKhau);

                    } else {
                        Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user, String taiKhoan, String sdt, String pass) {
        if (user == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("name", taiKhoan);
        data.put("email", user.getEmail());
        data.put("phone", sdt);
        data.put("pass", pass);
        data.put("role", "user");
        data.put("day_join", Timestamp.now());

        db.collection("users").document(user.getUid())
                .set(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi lưu dữ liệu!", Toast.LENGTH_SHORT).show());
    }
}
