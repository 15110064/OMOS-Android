package com.kt3.android.apidata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kt3.android.domain.Category;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by khoa1 on 4/15/2018.
 */

public class CategoryApiData {

    public static ArrayList<Category> getCategories(String dataJson)
    {
        Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Type type = new TypeToken<List<Category>>() {}.getType();
        Category categoryAll = new Category();
        categoryAll.setId(0L);
        categoryAll.setName("Xem tất cả");
        categoryAll.setCode("view-all");

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryAll);
        categories.addAll((Collection<? extends Category>) gson.fromJson(dataJson, type));
        return categories;
    }



}
