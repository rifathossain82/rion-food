package com.ecommerce.myapplicationtest.customer;

public class Add_item_cart {
    String hotel_id,customer_email,f_name,f_price,f_details,f_photo,f_quantity;

    public Add_item_cart() {
    }

    public Add_item_cart(String hotel_id, String customer_email, String f_name, String f_price, String f_details, String f_photo, String f_quantity) {
        this.hotel_id = hotel_id;
        this.customer_email = customer_email;
        this.f_name = f_name;
        this.f_price = f_price;
        this.f_details = f_details;
        this.f_photo = f_photo;
        this.f_quantity = f_quantity;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
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

    public String getF_photo() {
        return f_photo;
    }

    public void setF_photo(String f_photo) {
        this.f_photo = f_photo;
    }

    public String getF_quantity() {
        return f_quantity;
    }

    public void setF_quantity(String f_quantity) {
        this.f_quantity = f_quantity;
    }
}
