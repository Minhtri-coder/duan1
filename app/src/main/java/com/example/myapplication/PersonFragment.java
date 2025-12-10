package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class PersonFragment extends Fragment {

    RelativeLayout btnInfo, btnUpdate, btnChangePass;
    Button btnLogout;

    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // ✅ Ánh xạ đúng ID từ XML của bạn
        btnInfo = view.findViewById(R.id.btnInfo);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnLogout = view.findViewById(R.id.btnLogout);

        auth = FirebaseAuth.getInstance();

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
