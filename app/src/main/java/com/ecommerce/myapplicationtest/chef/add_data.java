package com.ecommerce.myapplicationtest.chef;

import android.net.Uri;

public class add_data {
    String name,price,details,filepath,email;

    public add_data() {
    }

    public add_data(String name, String price, String details, String filepath,String email) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.filepath = filepath;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
