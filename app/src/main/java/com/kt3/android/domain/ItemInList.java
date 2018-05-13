package com.kt3.android.domain;

import java.math.BigDecimal;

/**
 * Created by khoa1 on 2/18/2018.
 */

public class ItemInList {
    private int id;
    private String name;
    private int image;
    private BigDecimal price;

    public ItemInList() {
    }

    public ItemInList(int id, String name, int image, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
