package com.example.myapplication.Model;

public class CartItem {
    private String name;
    private double price;
    private int quantity;
    private String image;

    public CartItem(String name, double price, int quantity, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImage() { return image; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
