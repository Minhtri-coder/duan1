package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edtOldPass, edtNewPass, edtConfirmPass;
    Button btnUpdatePass;
    ImageButton btnBack;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // ✅ Ánh xạ đúng ID từ layout của bạn
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnUpdatePass = findViewById(R.id.btnUpdatePass);
        btnBack = findViewById(R.id.btn_back_password);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // ✅ Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // ✅ Nút cập nhật mật khẩu
        btnUpdatePass.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {

        String oldPass = edtOldPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        if (TextUtils.isEmpty(oldPass) ||
                TextUtils.isEmpty(newPass) ||
                TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = user.getEmail();

        AuthCredential credential =
                EmailAuthProvider.getCredential(email, oldPass);

        // ✅ Xác thực lại mật khẩu cũ
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                // ✅ Đổi sang mật khẩu mới
                user.updatePassword(newPass).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
