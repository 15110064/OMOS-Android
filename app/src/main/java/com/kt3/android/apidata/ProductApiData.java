package com.kt3.android.apidata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kt3.android.domain.Product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductApiData {
    public static ArrayList<Product> getProducts(String dataJson)
    {
        Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Type type = new TypeToken<List<Product>>() {}.getType();
        ArrayList<Product> products = gson.fromJson(dataJson, type);
        return products;
    }
}
