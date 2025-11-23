    package com.example.myapplication;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QuerySnapshot;
    // Không cần Firestore nữa, nên có thể xóa import dưới
    // import com.google.firebase.firestore.FirebaseFirestore;

    public class LoginActivity extends AppCompatActivity {

        // Khai báo các View từ layout
        private EditText edtUsername, edtPassword; // edtUsername sẽ nhận Email
        private Button btnLogin, btnSignUp;

        // Khai báo Firebase Instances
        private FirebaseAuth mAuth;
        // Không cần Firestore để đăng nhập bằng Email
         private FirebaseFirestore db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            // 1. Khởi tạo Firebase
            mAuth = FirebaseAuth.getInstance();
             db = FirebaseFirestore.getInstance(); // KHÔNG CẦN THIẾT NỮA

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
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                assert mAuth.getCurrentUser() != null;
                                db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                        String role = document.getString("role");
                                        if ("admin".equalsIgnoreCase(role)){
                                            Toast.makeText(LoginActivity.this, "Vào trang Admin", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, AdminMain.class));
                                        }else{
                                            Toast.makeText(LoginActivity.this, "Vào trang user", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        }
                                        finish();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }