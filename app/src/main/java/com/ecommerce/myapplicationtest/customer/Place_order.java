package com.ecommerce.myapplicationtest.customer;

public class Place_order {
    String id,address,cat,h_id,f_name,f_price,h_name,status,method,date;

    public Place_order() {
    }

    public Place_order(String id, String address, String cat, String h_id, String f_name, String f_price, String h_name, String status, String method,String date) {
        this.id = id;
        this.address = address;
        this.cat = cat;
        this.h_id = h_id;
        this.f_name = f_name;
        this.f_price = f_price;
        this.h_name = h_name;
        this.status = status;
        this.method = method;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getH_id() {
        return h_id;
    }

    public void setH_id(String h_id) {
        this.h_id = h_id;
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

    public String getH_name() {
        return h_name;
    }

    public void setH_name(String h_name) {
        this.h_name = h_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
