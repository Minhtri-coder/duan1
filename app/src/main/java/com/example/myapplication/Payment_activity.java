package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Api.CreateOrder;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class Payment_activity extends AppCompatActivity {
    TextView txttotal,txtsubtotal;
    RadioButton rb_zalo,rb_cod;
    Button btnpay;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        txttotal= findViewById(R.id.txttotal);
        txtsubtotal= findViewById(R.id.txtsubtotal);
        rb_cod= findViewById(R.id.rb_cod);
        rb_zalo= findViewById(R.id.rb_zalo);
        btnpay= findViewById(R.id.btnpay);
        Intent intent=getIntent();
        txtsubtotal.setText(intent.getStringExtra("tongtien"));
        String subtotalStr = txtsubtotal.getText().toString()
                .replace(".", "")   // loại bỏ dấu chấm
                .replace("₫", "")   // loại bỏ ký hiệu đồng
                .trim();

        double subtotal = Double.parseDouble(subtotalStr);
        double shipping = 50000; // hoặc lấy từ txtShipping theo cách tương tự
        double total = subtotal + shipping;

        txttotal.setText(String.format("%,.0f ₫", total)); // format lại với dấu chấm
        String totalString= String.format("%.0f", total);
        // su kien radiobutton
        rb_zalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_zalo.setChecked(true);
                rb_cod.setChecked(false);
            }
        });
        rb_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_zalo.setChecked(false);
                rb_cod.setChecked(true);
            }
        });
        // zalo pay ne
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // su ly su kien click pay
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rb_zalo.isChecked() && !rb_cod.isChecked()) {
                    Toast.makeText(Payment_activity.this,
                            "Vui lòng chọn phương thức thanh toán",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rb_zalo.isChecked()) {
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        JSONObject data = orderApi.createOrder(totalString);
                        String code = data.getString("return_code");
                        Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();
                        if (code.equals("1")) {
                            String token = data.getString("zp_trans_token");
                            ZaloPaySDK.getInstance().payOrder(Payment_activity.this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(String s, String s1, String s2) {
                                    Log.d("THANH_TOAN", "Payment Success:"
                                            + "\nTransaction ID: " + s
                                            + "\nAppTransID: " + s1
                                            + "\nMessage: " + s2);
                                    Intent intent2= new Intent(Payment_activity.this,payment_succesful.class);
                                    intent2.putExtra("result", "Payment Successful");
                                    intent2.putExtra("total", totalString);
                                    startActivity(intent2);

                                }

                                @Override
                                public void onPaymentCanceled(String s, String s1) {
                                    Intent intent2= new Intent(Payment_activity.this,payment_succesful.class);
                                    intent2.putExtra("result", "Payment Canceled");
                                    startActivity(intent2);
                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                    Intent intent2= new Intent(Payment_activity.this,payment_succesful.class);
                                    intent2.putExtra("result", "Payment Failed");
                                    startActivity(intent2);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (rb_cod.isChecked()) {
                    Intent intent2= new Intent(Payment_activity.this,payment_succesful.class);
                    intent2.putExtra("result", "Payment Successful");
                    intent2.putExtra("total", totalString);
                    startActivity(intent2);
                }
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}