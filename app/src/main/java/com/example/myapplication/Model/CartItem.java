package com.example.myapplication.Model;

public class CartItem {

    private String productId;   // ✅ ID SẢN PHẨM (BẮT BUỘC)
    private String name;
    private double price;
    private int quantity;
    private String image;

    public CartItem() {}

    public CartItem(String productId, String name, double price, int quantity, String image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    // ===== GETTER =====
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }

    // ===== SETTER =====
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
