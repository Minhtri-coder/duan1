package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText edtTaiKhoan, edtEmail, edtSDT, edtMatKhau;
    private Button btnDangKy, btnTroVe;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 2. Ánh xạ View
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnTroVe = findViewById(R.id.btnTroVe);

        // 3. Sự kiện nút Đăng ký
        btnDangKy.setOnClickListener(v -> attemptRegistration());

        // 4. Sự kiện nút Trở về
        btnTroVe.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {
        final String tenTaiKhoan = edtTaiKhoan.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String sdt = edtSDT.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        // Kiểm tra nhập liệu
        if (email.isEmpty() || matKhau.isEmpty() || tenTaiKhoan.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (matKhau.length() < 3) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo tài khoản Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, matKhau)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "Đăng ký Firebase Auth thành công: " + user.getEmail());

                        // Tiếp tục lưu thông tin lên Firestore
                        saveUserDataToFirestore(user, tenTaiKhoan, sdt);
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, "Đăng ký Firebase Auth thất bại: ", e);
                        Toast.makeText(RegisterActivity.this,
                                "Đăng ký Firebase thất bại: " + (e != null ? e.getMessage() : "Không rõ nguyên nhân."),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserDataToFirestore(FirebaseUser user, String tenTaiKhoan, String sdt) {
        if (user == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy người dùng sau khi đăng ký.", Toast.LENGTH_LONG).show();
            return;
        }

        // Chuẩn bị dữ liệu lưu Firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("ten_tai_khoan", tenTaiKhoan);
        userData.put("email", user.getEmail());
        userData.put("so_dien_thoai", sdt);
        userData.put("ngay_tham_gia", Timestamp.now());

        // Ghi vào Firestore
        db.collection("users").document(user.getUid())
                .set(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Lưu thông tin Firestore thành công.");
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang LoginActivity
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, "Lỗi khi lưu Firestore: ", e);
                        Toast.makeText(RegisterActivity.this,
                                "Đăng ký thành công nhưng lưu thông tin thất bại: " + (e != null ? e.getMessage() : "Không rõ nguyên nhân."),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
