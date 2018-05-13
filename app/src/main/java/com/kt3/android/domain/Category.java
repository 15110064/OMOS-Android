package com.kt3.android.domain;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by khoa1 on 4/15/2018.
 */

public class Category implements Serializable {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String image;
    private Date updateDate;
    private boolean status;
    //private Category parent;
    //private List<Category> childs = new ArrayList<>();
    //private List<Product> products  = new ArrayList<>();

    public Category() {
    }

    public Category(Long id, String name, String code, String description, String image, Date updateDate, boolean status) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.image = image;
        this.updateDate = updateDate;
        this.status = status;
        //this.parent = parent;
        //this.childs = childs;
       // this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

//    public Category getParent() {
//        return parent;
//    }
//
//    public void setParent(Category parent) {
//        this.parent = parent;
//    }

//    public List<Category> getChilds() {
//        return childs;
//    }
//
//    public void setChilds(List<Category> childs) {
//        this.childs = childs;
//    }

   // public List<Product> getProducts() {
//        return products;
//    }

    //public void setProducts(List<Product> products) {
//        this.products = products;
//    }
}
