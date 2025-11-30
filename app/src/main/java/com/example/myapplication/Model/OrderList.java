package com.example.myapplication.Model;

public class OrderList {
    private String orderID;
    private String creatAt;
    private String status;
    private int total;
    private String userId;
    private String PaymentMethod;

    public OrderList() {
    }

    public OrderList(String orderID, String creatAt, String status, int total) {
        this.orderID = orderID;
        this.creatAt = creatAt;
        this.status = status;
        this.total = total;
    }

    public OrderList(String orderID, String creatAt, String status, int total, String userId) {
        this.orderID = orderID;
        this.creatAt = creatAt;
        this.status = status;
        this.total = total;
        this.userId = userId;
    }

    public OrderList(String orderID, String creatAt, String status, int total, String userId, String paymentMethod) {
        this.orderID = orderID;
        this.creatAt = creatAt;
        this.status = status;
        this.total = total;
        this.userId = userId;
        PaymentMethod = paymentMethod;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(String creatAt) {
        this.creatAt = creatAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }
}