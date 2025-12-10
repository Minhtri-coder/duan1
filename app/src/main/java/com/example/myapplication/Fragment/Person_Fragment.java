package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ChangePasswordActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.UpdateAccountActivity;
import com.example.myapplication.ViewAccountActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Person_Fragment extends Fragment {

    RelativeLayout btnInfo, btnUpdate, btnChangePass;
    Button btnLogout;
    TextView txtName, txtEmail;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // ✅ Ánh xạ view
        btnInfo = view.findViewById(R.id.btnInfo);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnLogout = view.findViewById(R.id.btnLogout);

        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // ✅ HIỂN THỊ EMAIL (FirebaseAuth) + NAME (Firestore)
        if (user != null) {
            String uid = user.getUid();

            // ✅ Email lấy từ FirebaseAuth
            txtEmail.setText(user.getEmail());

            // ✅ Name lấy từ Firestore
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            if (name != null && !name.isEmpty()) {
                                txtName.setText(name);   // ✅ HIỆN ĐÚNG "Lmao"
                            } else {
                                txtName.setText("Người dùng");
                            }
                        } else {
                            txtName.setText("Người dùng");
                        }
                    })
                    .addOnFailureListener(e -> {
                        txtName.setText("Người dùng");
                    });
        }

        // ✅ Xem thông tin tài khoản
        btnInfo.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ViewAccountActivity.class))
        );

        // ✅ Cập nhật thông tin
        btnUpdate.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), UpdateAccountActivity.class))
        );

        // ✅ Đổi mật khẩu
        btnChangePass.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class))
        );

        // ✅ Đăng xuất
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}
