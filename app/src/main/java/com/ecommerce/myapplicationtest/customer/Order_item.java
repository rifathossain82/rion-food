package com.ecommerce.myapplicationtest.customer;

public class Order_item {
    String f_name,f_price,f_details,f_quantity,f_photo;

    public Order_item() {
    }

    public Order_item(String f_name, String f_price, String f_details, String f_quantity, String f_photo) {
        this.f_name = f_name;
        this.f_price = f_price;
        this.f_details = f_details;
        this.f_quantity = f_quantity;
        this.f_photo = f_photo;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_price() {
        return f_price;
    }

    public void setF_price(String f_price) {
        this.f_price = f_price;
    }

    public String getF_details() {
        return f_details;
    }

    public void setF_details(String f_details) {
        this.f_details = f_details;
    }

    public String getF_quantity() {
        return f_quantity;
    }

    public void setF_quantity(String f_quantity) {
        this.f_quantity = f_quantity;
    }

    public String getF_photo() {
        return f_photo;
    }

    public void setF_photo(String f_photo) {
        this.f_photo = f_photo;
    }
}
