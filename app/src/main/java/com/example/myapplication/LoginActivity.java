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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// Không cần Firestore nữa, nên có thể xóa import dưới
// import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Khai báo các View từ layout
    private EditText edtUsername, edtPassword; // edtUsername sẽ nhận Email
    private Button btnLogin, btnSignUp;

    // Khai báo Firebase Instances
    private FirebaseAuth mAuth;
    // Không cần Firestore để đăng nhập bằng Email
    // private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        // db = FirebaseFirestore.getInstance(); // KHÔNG CẦN THIẾT NỮA

        // 2. Tham chiếu Views
        edtUsername = findViewById(R.id.edtUsername); // Sẽ dùng để nhập Email
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // 3. Xử lý sự kiện nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // 4. Xử lý sự kiện nút Đăng ký (chuyển sang RegisterActivity)
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    // Tự động kiểm tra người dùng đã đăng nhập chưa khi màn hình bắt đầu
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // Nếu đã đăng nhập, chuyển hướng đến màn hình chính ngay lập tức
            navigateToMainScreen();
        }
    }

    private void attemptLogin() {
        // Lấy Email và Mật khẩu
        final String email = edtUsername.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bắt đầu quá trình đăng nhập Firebase Auth (KHÔNG CẦN TÌM KIẾM FIRESTORE)
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            navigateToMainScreen();

                        } else {
                            // Đăng nhập thất bại
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Đăng nhập thất bại. Vui lòng kiểm tra Email/Mật khẩu.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Phương thức chuyển hướng đến màn hình chính
    private void navigateToMainScreen() {
        // THAY THẾ MainActivity.class bằng Activity màn hình chính của bạn
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa hết Activity trước đó
        startActivity(intent);
        finish();
    }
}