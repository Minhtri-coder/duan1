package com.example.myapplication.Model;

public class CartItem {
    private String productId;
    private String name;
    private double price;
    private int quantity;
    private String image;   // ✅ ẢNH SẢN PHẨM

    public CartItem() {}

    public CartItem(String productId, String name, double price, int quantity, String image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public CartItem(String name, double price, int quantity, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImage() { return image; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
