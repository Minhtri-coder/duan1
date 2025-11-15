package com.example.myapplication.Model;

public class item_category {
    int img;
    String name;

    public item_category(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public item_category() {
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
}
