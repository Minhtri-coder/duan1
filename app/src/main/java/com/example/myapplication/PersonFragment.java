package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PersonFragment extends Fragment {

    TextView txtName, txtLogout;
    LinearLayout btnAccount, btnUpdate, btnChangePass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtLogout = view.findViewById(R.id.txtLogout);

        btnAccount = view.findViewById(R.id.btnAccount);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnChangePass = view.findViewById(R.id.btnChangePass);

        // TÊN NGƯỜI DÙNG -> sau này bạn lấy từ Firebase
        txtName.setText("Trương Nguyên Quỳnh Nhân");

        // Các nút bấm
        btnAccount.setOnClickListener(v -> {
            // Code mở trang Tài khoản
        });

        btnUpdate.setOnClickListener(v -> {
            // Code mở trang cập nhật thông tin
        });

        btnChangePass.setOnClickListener(v -> {
            // Code mở trang đổi mật khẩu
        });

        txtLogout.setOnClickListener(v -> {
            // FirebaseAuth.getInstance().signOut();
            // Chuyển về màn hình Login
        });

        return view;
    }
}
