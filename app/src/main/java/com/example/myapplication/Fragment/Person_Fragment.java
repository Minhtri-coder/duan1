package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.UpdateAccountActivity;
import com.example.myapplication.ViewAccountActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Person_Fragment extends Fragment {

    TextView txtName, txtLogout;
    LinearLayout btnAccount, btnUpdate, btnChangePass;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ
        txtName = view.findViewById(R.id.txtName);
        txtLogout = view.findViewById(R.id.txtLogout);
        btnAccount = view.findViewById(R.id.btnAccount);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnChangePass = view.findViewById(R.id.btnChangePass);

        // =========================
        // ✅ HIỂN THỊ TÊN NGƯỜI DÙNG
        // =========================
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();

            db.collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");

                            if (name != null && !name.isEmpty()) {
                                txtName.setText(name);
                            } else {
                                txtName.setText(mAuth.getCurrentUser().getEmail());
                            }

                        } else {
                            txtName.setText("Chưa có dữ liệu");
                        }
                    })
                    .addOnFailureListener(e -> {
                        txtName.setText("Lỗi tải tên");
                    });

        } else {
            txtName.setText("Chưa đăng nhập");
        }

        // =========================
        // Nút xem tài khoản
        // =========================
        btnAccount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ViewAccountActivity.class));
        });

        // =========================
        // Nút cập nhật thông tin
        // =========================
        btnUpdate.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdateAccountActivity.class));
        });

        // =========================
        // NÚT ĐỔI MẬT KHẨU
        // =========================
        btnChangePass.setOnClickListener(v -> showChangePasswordDialog());

        // =========================
        // NÚT ĐĂNG XUẤT
        // =========================
        txtLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    // ======================
    // ✅ HÀM ĐỔI MẬT KHẨU
    // ======================
    private void showChangePasswordDialog() {

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Nhập mật khẩu mới (>= 6 ký tự)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Đổi mật khẩu")
                .setView(input)
                .setPositiveButton("Đổi", (dialog, which) -> {
                    String newPass = input.getText().toString().trim();

                    if (newPass.length() < 6) {
                        Toast.makeText(getContext(),
                                "Mật khẩu phải >= 6 ký tự",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.getCurrentUser().updatePassword(newPass)
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(getContext(),
                                            "Đổi mật khẩu thành công",
                                            Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(),
                                            "Lỗi: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
