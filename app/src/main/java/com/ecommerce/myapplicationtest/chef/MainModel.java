package com.ecommerce.myapplicationtest.chef;

public class MainModel {
    Integer logo_e;
    String name_e;

    public MainModel(Integer logo_e, String name_e) {
        this.logo_e = logo_e;
        this.name_e = name_e;
    }

    public Integer getLogo_e() {
        return logo_e;
    }

    public String getName_e() {
        return name_e;
    }
}
