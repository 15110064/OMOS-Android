package com.kt3.android.adapter;

import com.kt3.android.CategoryExploreActivity;
import com.kt3.android.R;
import com.kt3.android.domain.Category;
import com.kt3.android.domain.CategoryItem;
import com.kt3.android.interfaces.ObserAdapterClick;
import com.kt3.android.interfaces.ViewHolderSubject;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khoa1 on 2/20/2018.
 * Lớp này là Adapter cho List các Category ở Activity CategoryExplorer
 */

public class CategoryListRecyclerViewAdapter extends RecyclerView.Adapter<CategoryListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Category> categoryItemArrayList;
    private Context context;

    // data is passed into the constructor
    public CategoryListRecyclerViewAdapter(Context context, ArrayList<Category> objects) {
        this.categoryItemArrayList = objects;
        this.context = context;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_explore, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.add((ObserAdapterClick) context);
        return viewHolder;
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        CategoryItem categoryItem = categoryItemArrayList.get(position);
//        holder.img_category.setImageResource(categoryItem.getImage());
//        holder.tvCategory.setText(categoryItem.getName());
        Category categoryItem = categoryItemArrayList.get(position);
        if(categoryItem.getId() == 0){
            Picasso.with(context)
                    .load(R.drawable.history1)
                    .resize(300, 300)
                    .centerCrop()
                    .into(holder.img_category);
        }
        else {
            Picasso.with(context)
                    // .load("http://10.0.2.2:8080" + categoryItem.getImage())
                    .load(categoryItem.getImage())
                    .resize(300, 300)
                    .centerCrop()
                    .into(holder.img_category);
        }
        holder.tvCategory.setText(categoryItem.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return categoryItemArrayList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ViewHolderSubject {
        TextView tvCategory;
        ImageView img_category;
        List<ObserAdapterClick> obserAdapterClicks;

        public ViewHolder(View itemView) {
            super(itemView);
            obserAdapterClicks = new ArrayList<>();
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            img_category = (ImageView) itemView.findViewById(R.id.img_category);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            notifyChange();

        }


        @Override
        public void add(ObserAdapterClick obserAdapterClick) {
            obserAdapterClicks.add(obserAdapterClick);
        }

        @Override
        public void remove(ObserAdapterClick obserAdapterClick) {
            obserAdapterClicks.remove(obserAdapterClick);
        }

        @Override
        public void notifyChange() {
            for (ObserAdapterClick obserAdapterClick: obserAdapterClicks
                    ) {
                obserAdapterClick.update(categoryItemArrayList.get(getAdapterPosition()).getId());
            }
        }
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}