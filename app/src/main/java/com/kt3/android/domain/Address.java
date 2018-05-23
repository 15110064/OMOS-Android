package com.kt3.android.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by 97lynk on 22/02/2018.
 */

public class Address implements Serializable, Comparable<Address> {
    private int id;

    private String fullName;

    private String phone;

    private String address;

    private int cityID;

    private int provinceID;

    private int wardID;

    private String cityName;

    private String provinceName;

    private String wardName;

//    private int account_id;


    public Address() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getWardID() {
        return wardID;
    }

    public void setWardID(int wardID) {
        this.wardID = wardID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

//    public int getAccount_id() {
//        return account_id;
//    }
//
//    public void setAccount_id(int account_id) {
//        this.account_id = account_id;
//    }

    @Override
    public int compareTo(@NonNull Address o) {
        return this.fullName.toUpperCase().compareTo(o.fullName.toUpperCase());
    }

    @Override
    public String toString() {
        return address + ", phường/xã " + wardName + ", quận/huyện " + provinceName + ", TP HCM";
    }
}
