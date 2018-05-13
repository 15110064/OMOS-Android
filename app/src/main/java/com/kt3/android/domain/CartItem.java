package com.kt3.android.domain;

import com.kt3.android.enums.ICE_LEVEL;
import com.kt3.android.enums.SUGAR_LEVEL;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {


    private int id;


    private ICE_LEVEL iceLevel;


    private SUGAR_LEVEL sugarLevel;

    private int quantity;

    private BigDecimal subTotal;


   private Product product;


    private Cart cart;


    public CartItem() {
    }

    public CartItem(ICE_LEVEL iceLevel, SUGAR_LEVEL sugarLevel, int quantity, BigDecimal subTotal) {
        this.iceLevel = iceLevel;
        this.sugarLevel = sugarLevel;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ICE_LEVEL getIceLevel() {
        return iceLevel;
    }

    public void setIceLevel(ICE_LEVEL iceLevel) {
        this.iceLevel = iceLevel;
    }

    public SUGAR_LEVEL getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(SUGAR_LEVEL sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
}