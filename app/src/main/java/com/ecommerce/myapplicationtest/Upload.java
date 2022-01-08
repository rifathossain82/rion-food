package com.ecommerce.myapplicationtest;

public class Upload {
    private String name;
    private String image_uri;

    public Upload() {
    }

    public Upload(String name, String image_uri) {
        if(name.trim().equals("")){
            name="No name";
        }
        this.name = name;
        this.image_uri = image_uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
