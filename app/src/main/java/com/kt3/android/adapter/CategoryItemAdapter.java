package com.kt3.android.adapter;

import com.kt3.android.CategoryExploreActivity;
import com.kt3.android.R;
import com.kt3.android.domain.Category;
import com.kt3.android.domain.CategoryItem;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by khoa1 on 2/18/2018.
 */


public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemHolder> {

    private ArrayList<Category> categoryItemArrayList;
    private Context context;

    public CategoryItemAdapter(@NonNull Context context, @NonNull ArrayList<Category> objects) {

        this.categoryItemArrayList = objects;
        this.context = context;
    }

    @Override
    public CategoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_category, parent, false);
        return new CategoryItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryItemHolder holder, int position) {
        Category categoryItem = categoryItemArrayList.get(position);
        holder.tvCategory.setText(categoryItem.getName());
        Log.i("Name-Debug", categoryItem.getName());
        Picasso.with(context)
                //.load("http://192.168.137.1:8080" + categoryItem.getImage())
                .load(categoryItem.getImage())
                .into(holder.img_category);
        if(categoryItem.getId() == 0){
            Picasso.with(context)
                    .load(R.drawable.history1)
                    .resize(500, 250)
                    .centerCrop()
                    .into(holder.img_category);
        }
    }

    @Override
    public int getItemCount() {
        return categoryItemArrayList.size();
    }


    class CategoryItemHolder extends RecyclerView.ViewHolder {

        TextView tvCategory;
        ImageView img_category;

        public CategoryItemHolder(View itemView) {
            super(itemView);

            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            img_category = (ImageView) itemView.findViewById(R.id.img_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CategoryExploreActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
