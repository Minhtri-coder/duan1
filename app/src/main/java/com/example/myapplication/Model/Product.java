package com.example.myapplication.Model;

public class Product {
    String productId;
    String productName;
    String productImage;
    String productDescription;
    String price;

    public Product() {
    }

    public Product(String productId, String productName, String productImage, String productDescription, String price) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.price = price;
    }

    public Product(String productName, String price, String productImage, String productDescription) {
        this.productName = productName;
        this.price = price;
        this.productImage = productImage;
        this.productDescription = productDescription;
    }

    public Product(String productId, String productName, String productImage) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
