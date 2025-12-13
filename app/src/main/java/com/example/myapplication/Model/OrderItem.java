package com.example.myapplication.Model;

public class OrderItem {
    int img;
    String name;
    int price;
    int quantity;

    public OrderItem() {}

    public OrderItem(int img, String name, int price, int quantity) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
