package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CheckoutFragment extends Fragment {

    private TextView txtName, btnLogout;
    private ImageView imgAvatar;
    private LinearLayout itemTaiKhoan, itemCapNhat, itemDoiMK;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtName = view.findViewById(R.id.txtName);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        btnLogout = view.findViewById(R.id.btnLogout);

        itemTaiKhoan = view.findViewById(R.id.itemTaiKhoan);
        itemCapNhat = view.findViewById(R.id.itemCapNhat);
        itemDoiMK = view.findViewById(R.id.itemDoiMK);

        loadUserInfo();

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        return view;
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        txtName.setText(name);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Không tải được thông tin", Toast.LENGTH_SHORT).show());
    }
}
