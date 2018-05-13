package com.kt3.android.pseudodata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.kt3.android.adapter.CategoryItemAdapter;
import com.kt3.android.domain.Category;

import java.util.ArrayList;

/**
 * Created by khoa1 on 2/18/2018.
 */

public class ItemCategoryData {

    public static RecyclerView.Adapter getCategoryItem(Context context, ArrayList<Category> categories)
    {
        CategoryItemAdapter categoryItemAdapter = new CategoryItemAdapter(context, categories);
        return  categoryItemAdapter;
    }


//    public static CategoryListRecyclerViewAdapter getCategoryItem1(Context context, int resource)
//    {
//        ArrayList<CategoryItem> categoryItemArrayList = new ArrayList<>();
//        categoryItemArrayList.add(new CategoryItem(0, "TS truyền thống", R.drawable.ts8));
//        categoryItemArrayList.add(new CategoryItem(3, "TS Kem", R.drawable.ts6));
//        categoryItemArrayList.add(new CategoryItem(1, "TS Trân Châu", R.drawable.ts9));
//        categoryItemArrayList.add(new CategoryItem(4, "TS Hoa Quả", R.drawable.ts2));
//        categoryItemArrayList.add(new CategoryItem(5, "TS Đặc Biệt", R.drawable.ts5));
//        categoryItemArrayList.add(new CategoryItem(6, "TS Topping", R.drawable.ts7));
//        categoryItemArrayList.add(new CategoryItem(7, "TS Hồng Trà", R.drawable.ts4));
//        categoryItemArrayList.add(new CategoryItem(8, "TS Người lớn", R.drawable.ts1));
//        //CategoryListRecyclerViewAdapter categoryItemAdapter = new CategoryListRecyclerViewAdapter(context, categoryItemArrayList);
//
//        return  categoryItemAdapter;
//    }
}
