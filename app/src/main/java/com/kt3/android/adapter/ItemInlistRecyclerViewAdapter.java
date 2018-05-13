package com.kt3.android.adapter;

import com.kt3.android.CategoryExploreActivity;
import com.kt3.android.FoodDetailActivity;
import com.kt3.android.R;
import com.kt3.android.domain.ItemInList;
import com.kt3.android.domain.Product;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by khoa1 on 2/20/2018.
 */


//Class này là Apdapter cho Danh sách sản phẩm
public class ItemInlistRecyclerViewAdapter extends RecyclerView.Adapter<ItemInlistRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Product> categoryItemArrayList;
    private Context context;


    // data is passed into the constructor
    public ItemInlistRecyclerViewAdapter(Context context, ArrayList<Product> objects) {
        this.categoryItemArrayList = objects;
        this.context = context;
    }



    // inflates the row layout from xml when needed
    @Override
    public ItemInlistRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_in_list, parent, false);
        ItemInlistRecyclerViewAdapter.ViewHolder viewHolder = new ItemInlistRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(final ItemInlistRecyclerViewAdapter.ViewHolder holder, final int position) {
        Product categoryItem = categoryItemArrayList.get(position);
        holder.txt_list_food_name.setText(categoryItem.getName());
        Picasso.with(context)
                //.load("http://10.0.2.2:8080" + categoryItem.getThumbnail())
                .load(categoryItem.getThumbnail())
                .resize(300, 300)
                .centerCrop()
                .into(holder.list_img_food);
        holder.item_food_cost.setText(categoryItem.getPrice().toString());
        Log.i("Item", "Added Item");
        //chac co attribute nao null bi lay ra ko, de t coi lai , ngu di
        //:vcoi lai domain urrl dda



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return categoryItemArrayList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txt_list_food_name, item_food_cost;
        ImageView list_img_food, imgAddToCart;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_list_food_name = (TextView) itemView.findViewById(R.id.txt_list_food_name);
            list_img_food = (ImageView) itemView.findViewById(R.id.list_img_food);
            imgAddToCart = (ImageView) itemView.findViewById(R.id.imgAddToCart);
            item_food_cost = (TextView) itemView.findViewById(R.id.item_food_cost);
            itemView.setOnClickListener(this);
            imgAddToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == imgAddToCart.getId()){
                ((CategoryExploreActivity) context).showChooseOptionDialog(categoryItemArrayList.get(getAdapterPosition()));
                //Toast.makeText(view.getContext(), "Added this item into Cart", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(view.getContext(), FoodDetailActivity.class);
                Log.i("Click", "onClick: " + getAdapterPosition());
                intent.putExtra("product", categoryItemArrayList.get(getAdapterPosition()));
                view.getContext().startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onRowClicked(View view, int position);
        void onViewClicked(View view);
    }


}