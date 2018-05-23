package com.kt3.android.domain;

import com.kt3.android.enums.ORDER_STATUS;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class OrderTable implements Serializable {

    private int id;

    private BigDecimal totalPrice;

    private long createIn;

    private long updateDate;

    private String code;

    private ORDER_STATUS order_status;

    private Address address;

    public OrderTable() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public ORDER_STATUS getOrder_status() {
        return order_status;
    }

    public void setOrder_status(ORDER_STATUS order_status) {
        this.order_status = order_status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCreateIn() {
        return createIn;
    }

    public void setCreateIn(long createIn) {
        this.createIn = createIn;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }
}

