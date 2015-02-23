package com.crandall.best.practices.models;


public class Product {

    /**
     * The URL for the image of the product.
     */
    private String mImageUrl;
    /**
     * The name of the product.
     */
    private String mProductName;
    /**
     * The description of the product.
     */
    private String mProductDescription;

    public Product(String imageUrl, String productName, String productDescription) {
        this.mImageUrl = imageUrl;
        this.mProductName = productName;
        this.mProductDescription = productDescription;

    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        this.mProductName = productName;
    }

    public String getProductDescription() {
        return mProductDescription;
    }

    public void setProductDescription(String productDescription) {
        this.mProductDescription = productDescription;
    }
}
