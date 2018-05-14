package com.kt3.android.domain;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kt3.android.adapter.CartItemAdapter;
import com.kt3.android.other.ConstantData;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by 97lynk on 13/05/2018.
 */

public class CartSubject extends Observable {
    private static CartSubject OBJECT;
    private Cart cart;
//    private Context context;

    private CartSubject() {
//        this.context = context;
    }

    public static CartSubject getIntanse() {
        if (OBJECT == null)
            OBJECT = new CartSubject();
        return OBJECT;
    }


    public ArrayList<CartItem> getCartItems() {
        return cart.getCartItems();
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cart.setCartItems(cartItems);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void calculateTotalPrice() {
        double sum = 0.0;
        for (CartItem c : cart.getCartItems()) {
            sum += c.getQuantity() * c.getProduct().getPrice().doubleValue();
        }
        cart.setTotalPrice(new BigDecimal(sum));
        setChanged();
        notifyObservers();
    }
}
