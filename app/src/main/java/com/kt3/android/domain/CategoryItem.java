package com.kt3.android.domain;

/**
 * Created by khoa1 on 2/18/2018.
 */

public class CategoryItem {

    private int id;
    private String name;
    private int image;

    public CategoryItem() {
    }

    public CategoryItem(int id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
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
}
