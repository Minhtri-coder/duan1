package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private MaterialButton btnLogin;
    private TextView txtSignup, txtForgotPassword;

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
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        // đăng nhập
        btnLogin.setOnClickListener(v -> attemptLogin());

        // chuyển sang đăng ký
        txtSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // mở dialog quên mật khẩu
        txtForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void showForgotPasswordDialog() {

        View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        // ánh xạ trong dialog
        TextInputEditText edtEmailReset = view.findViewById(R.id.edtEmailReset);
        MaterialButton btnConfirmReset = view.findViewById(R.id.btnConfirmReset);
        TextView btnCancelReset = view.findViewById(R.id.btnCancelReset);
        ProgressBar progressBar = view.findViewById(R.id.progressBarReset);

        // nút hủy
        btnCancelReset.setOnClickListener(v -> dialog.dismiss());

        // nút gửi yêu cầu
        btnConfirmReset.setOnClickListener(v -> {

            String email = edtEmailReset.getText().toString().trim();

            if (email.isEmpty()) {
                edtEmailReset.setError("Email không được để trống");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            btnConfirmReset.setEnabled(false);

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {

                        progressBar.setVisibility(View.GONE);
                        btnConfirmReset.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Đã gửi link đặt lại mật khẩu đến: " + email,
                                    Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Email không tồn tại trong hệ thống!",
                                    Toast.LENGTH_LONG).show();
                        }

                    });
        });
    }


    private void attemptLogin() {

        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(auth -> {

                    // lấy role trong Firestore
                    db.collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(snapshot -> {

                                if (!snapshot.getDocuments().isEmpty()) {

                                    String role = snapshot.getDocuments().get(0).getString("role");

                                    if ("admin".equals(role)) {
                                        Toast.makeText(this, "Chào Admin!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, AdminMain.class));
                                    } else {
                                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                    }

                                } else {
                                    Toast.makeText(this,
                                            "Đăng nhập thành công nhưng không tìm thấy vai trò!",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                }

                                finish();
                            });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show());
    }
}
