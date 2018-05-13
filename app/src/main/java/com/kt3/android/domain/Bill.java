package com.kt3.android.domain;

import java.io.Serializable;

/**
 * Created by 97lynk on 22/02/2018.
 */

public class Bill implements Serializable{
    private String dateTime;
    private double total;
    private String address;
    private int img;
    private STATUS status ;

    public Bill(String dateTime, double total, String address, int img, STATUS status) {
        this.dateTime = dateTime;
        this.total = total;
        this.address = address;
        this.status = status;
        this.img = img;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

   public enum STATUS{
        DANG_VC, DA_GIAO, DANG_XL, DA_HUY
    }
}
