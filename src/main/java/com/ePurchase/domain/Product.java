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
    private List<String> feature = new ArrayList<>();
    private String amount;

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
}
