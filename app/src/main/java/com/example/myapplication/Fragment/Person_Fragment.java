package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.ViewAccountActivity;
import com.example.myapplication.UpdateAccountActivity;

import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class Person_Fragment extends Fragment {

    TextView txtName, txtLogout;
    LinearLayout btnAccount, btnUpdate, btnChangePass;
    FirebaseAuth mAuth;   // FirebaseAuth

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ
        txtName = view.findViewById(R.id.txtName);
        txtLogout = view.findViewById(R.id.txtLogout);
        btnAccount = view.findViewById(R.id.btnAccount);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnChangePass = view.findViewById(R.id.btnChangePass);

        // TÊN NGƯỜI DÙNG -> sau này lấy từ Firestore
        txtName.setText("Người dùng");

        // Nút mở trang tài khoản
        btnAccount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ViewAccountActivity.class));
        });


        // Nút cập nhật thông tin
        btnUpdate.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdateAccountActivity.class));
        });

        // Nút đổi mật khẩu
        btnChangePass.setOnClickListener(v -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() == null) {
                Toast.makeText(getContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo dialog nhập mật khẩu mới
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Đổi mật khẩu");

            final EditText input = new EditText(getContext());
            input.setHint("Nhập mật khẩu mới");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            builder.setView(input);

            builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                String newPass = input.getText().toString().trim();

                if (newPass.isEmpty()) {
                    Toast.makeText(getContext(), "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPass.length() < 6) {
                    Toast.makeText(getContext(), "Mật khẩu phải ≥ 6 ký tự!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tiến hành đổi mật khẩu
                auth.getCurrentUser().updatePassword(newPass)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            builder.show();
        });


        // Đăng xuất
        txtLogout.setOnClickListener(v -> {
            mAuth.signOut(); // đăng xuất Firebase

            Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish(); // đóng MainActivity
        });

        return view;
    }

    // ======================
    // HÀM ĐỔI MẬT KHẨU
    // ======================
    private void showChangePasswordDialog() {

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // EditText để nhập mật khẩu mới
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Nhập mật khẩu mới (>= 6 ký tự)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Đổi mật khẩu")
                .setView(input)
                .setPositiveButton("Đổi", (dialog, which) -> {
                    String newPass = input.getText().toString().trim();

                    if (newPass.length() < 6) {
                        Toast.makeText(getContext(), "Mật khẩu phải >= 6 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.getCurrentUser().updatePassword(newPass)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
