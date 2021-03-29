package com.appsnipp.androidproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Product {

    public int productId;
    public String productName;
    public String productQuantity;
    public int position;


    public Product() {

    }

    public int getPosition() {
        return position;
    }

    public Product(String productName, String productQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

}