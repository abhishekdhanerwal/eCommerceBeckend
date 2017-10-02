package com.ePurchase.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Get It on 9/14/2017.
 */

public class Product {

    private String id;
    private String imageUrl;
    private String asinId;
    private String title;
    private String itemURL;
    private List<String> feature = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private String brand;
    private String amount;
    private String reviewUrl;

    public Product() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<String> getFeature() {
        return feature;
    }

    public void setFeature(List<String> feature) {
        this.feature = feature;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAsinId() {
        return asinId;
    }

    public void setAsinId(String asinId) {
        this.asinId = asinId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }
}
