package com.ecommerce.myapplicationtest.customer;

public interface ShowOrder_interface {
    void show_data(String id,String h_id,String h_name,String payment_method,String status);
    void update_data(String id,String h_id,String h_name,String payment_method,String status);
}
