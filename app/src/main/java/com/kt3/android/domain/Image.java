package com.kt3.android.domain;

import java.io.Serializable;

/**
 * Created by khoa1 on 4/15/2018.
 */

public class Image implements Serializable {
    private Long Id;
    private String url;
    private Product product;

    public Image() {
    }

    public Image(Long id, String url, Product product) {
        Id = id;
        this.url = url;
        this.product = product;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
