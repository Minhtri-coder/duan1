package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.Model.OrderList;
import com.example.myapplication.util.mail;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
                                    Log.d("PAYMENT_SUCCESS", "Total: " + totalString);
//                                    Intent intent2= new Intent(Payment_activity.this,payment_succesful.class);
//                                    intent2.putExtra("result", "Payment Successful");
//                                    intent2.putExtra("total", totalString);
//                                    startActivity(intent2);
                                    // Lấy user ID
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    // Firestore reference
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    // Tạo ID đơn hàng
                                    String orderId = db.collection("orders").document().getId();

                                    // Tạo ngày giờ đẹp
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                    String timeString = sdf.format(new Date());

                                    // Lấy tổng tiền
                                    int total = Integer.parseInt(totalString);

                                    // Tạo object Order
                                    OrderList order = new OrderList(orderId, timeString, "Da thanh toan", total, userId, "Zalo pay");

                                    // Lưu lên Firestore
                                    db.collection("orders")
                                            .document(orderId)
                                            .set(order)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    // XÓA GIỎ HÀNG
                                                    SharedPreferences prefs = getSharedPreferences("CART_DATA", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = prefs.edit();
                                                    editor.clear();
                                                    editor.apply();
                                                    // Chuyển sang màn hình thành công
                                                    Intent intent2 = new Intent(Payment_activity.this, payment_succesful.class);
                                                    intent2.putExtra("result", "Payment Successful");
                                                    intent2.putExtra("total", totalString);
                                                    intent2.putExtra("orderId", orderId);
                                                    intent2.putExtra("timestamp", timeString);
                                                    startActivity(intent2);

                                                } else {
                                                    Toast.makeText(Payment_activity.this, "Lỗi tạo đơn hàng Firestore", Toast.LENGTH_SHORT).show();
                                                }
                                            });
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
                    // Lấy user ID
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // Firestore reference
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Tạo ID đơn hàng
                    String orderId = db.collection("orders").document().getId();
                    // Tạo ngày giờ đẹp
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    String timeString = sdf.format(new Date());
                    // Lấy tổng tiền
                    int total = Integer.parseInt(totalString);
                    OrderList order = new OrderList(orderId, timeString, "Da thanh toan", total, userId, "COD");
                        // Giả sử bạn có List<CartItem> cartItems chứa các sản phẩm trong giỏ hàng
                            String cartJson = getIntent().getStringExtra("cartList");
                            ArrayList<CartItem> cartList = new Gson().fromJson(cartJson,
                            new TypeToken<ArrayList<CartItem>>(){}.getType());
                    // 4. Lưu từng CartItem vào collection "order_details"
                    for (CartItem item : cartList) {
                        Map<String, Object> orderDetail = new HashMap<>();
                        orderDetail.put("orderID", orderId);        // ID đơn hàng
                        orderDetail.put("productId", item.getProductId());
                        orderDetail.put("productName", item.getName());
                        orderDetail.put("quantity", item.getQuantity());
                        orderDetail.put("price", item.getPrice());
                        orderDetail.put("productImage", item.getImage());  // nếu muốn lưu ảnh

                        // Thêm vào Firestore
                        db.collection("orderDetails")
                                .add(orderDetail)
                                .addOnSuccessListener(documentReference -> {
                                    // ✅ Lưu thành công
                                    System.out.println("Order detail added: " + documentReference.getId());
                                })
                                .addOnFailureListener(e -> {
                                    // ❌ Lưu thất bại
                                    e.printStackTrace();
                                });
                    }

                    //gui mail
                    db.collection("orders")
                            .document(orderId)
                            .set(order)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // XÓA GIỎ HÀNG
                                    SharedPreferences prefs = getSharedPreferences("CART_DATA", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();
                                    // send mail



                                    String subject = "Cảm ơn bạn đã đặt hàng tại MyShop!";
                                    String message = "<!DOCTYPE html>" +
                                            "<html>" +
                                            "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                                            "<h2 style='color: #2E8B57;'>Cảm ơn bạn đã đặt hàng!</h2>" +
                                            "<p>Đơn hàng của bạn sẽ sớm được giao. Dưới đây là thông tin chi tiết:</p>" +
                                            "<table style='border-collapse: collapse; width: 100%;'>" +
                                            "<tr><th style='border: 1px solid #ddd; padding: 8px;'>Sản phẩm</th>" +
                                            "<th style='border: 1px solid #ddd; padding: 8px;'>Số lượng</th>" +
                                            "<th style='border: 1px solid #ddd; padding: 8px;'>Giá</th></tr>";

                                    // Giả sử có cartList
                                    for (CartItem item : cartList) {
                                        message += "<tr>" +
                                                "<td style='border: 1px solid #ddd; padding: 8px;'>" + item.getName() + "</td>" +
                                                "<td style='border: 1px solid #ddd; padding: 8px;'>" + item.getQuantity() + "</td>" +
                                                "<td style='border: 1px solid #ddd; padding: 8px;'>" + item.getPrice() + " ₫</td>" +
                                                "</tr>";
                                    }

                                    message += "</table>" +
                                            "<p>Tổng tiền: <strong>" + txtsubtotal.getText().toString() + "</strong></p>" +
                                            "<p>Chúng tôi sẽ liên hệ với bạn khi đơn hàng được giao.</p>" +
                                            "<p style='color: #888;'>MyShop Team</p>" +
                                            "</body>" +
                                            "</html>";

                                    try {
                                        mail sender = new mail("truongnguyenquynhnhan@gmail.com", "udwcchvrktnkrhpe");
                                        sender.sendMail(subject, message, "truongnguyenquynhnhan@gmail.com", "vodaiminhtri@gmail.com");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    // Chuyển sang màn hình thành công
                                    Intent intent2 = new Intent(Payment_activity.this, payment_succesful.class);
                                    intent2.putExtra("result", "Payment Successful");
                                    intent2.putExtra("total", totalString);
                                    intent2.putExtra("orderId", orderId);
                                    intent2.putExtra("timestamp", timeString);
                                    startActivity(intent2);

                                } else {
                                    Toast.makeText(Payment_activity.this, "Lỗi tạo đơn hàng Firestore", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("ZALO_CALLBACK", "onNewIntent called");
        ZaloPaySDK.getInstance().onResult(intent);
    }
}