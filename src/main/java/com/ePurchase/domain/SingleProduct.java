package com.ePurchase.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Get It on 10/12/2017.
 */
public class SingleProduct {


    private String salePrice;
    private List<String> features = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private String color;
    private String size;
    private String detailUrl;
    private String reviewUrl;
    private String brand;
    private String title;
    private String aSIN;

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getaSIN() {
        return aSIN;
    }

    public void setaSIN(String aSIN) {
        this.aSIN = aSIN;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
