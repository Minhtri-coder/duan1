package com.example.myapplication.Model;

public class Orderdetails {

    private String nameProduct;
    private double priceProduct;
    private long quantityProduct;
    private String imgProduct;

    public Orderdetails(String nameProduct, double priceProduct, long quantityProduct, String imgProduct) {
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.quantityProduct = quantityProduct;
        this.imgProduct = imgProduct;
    }

    public Orderdetails() {
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public long getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(long quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }
}
