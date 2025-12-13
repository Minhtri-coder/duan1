package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Fragment.Home_Fragment;
import com.example.myapplication.Fragment.Order_details_Fragment;

public class payment_succesful extends AppCompatActivity {
    TextView txtresult,txtamount;
    Button btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_succesful);
        txtresult= findViewById(R.id.txtresult);
        txtamount= findViewById(R.id.txtamount);
        btnContinue= findViewById(R.id.btnContinue);

        Intent intent= getIntent();
        String result= intent.getStringExtra("result");
        String total= intent.getStringExtra("total");
        txtresult.setText(result);
        txtamount.setText(total);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(payment_succesful.this, MainActivity.class);
                startActivity(intent1);
            }
        });

    }
}