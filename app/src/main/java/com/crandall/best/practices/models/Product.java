package com.crandall.best.practices.models;


/**
 * Created by Crandall on 2/20/2015.
 */
public class Product {

    String imageUrl;
    String productName;
    String productDescription;

    public Product(String imageUrl, String productName, String productDescription){
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productDescription = productDescription;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
