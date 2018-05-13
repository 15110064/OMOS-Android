package com.kt3.android.adapter;

import com.kt3.android.R;
import com.kt3.android.domain.ItemInList;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by khoa1 on 2/18/2018.
 */

public class ItemInListAdapter extends ArrayAdapter {

    private int resource;
    private ArrayList<ItemInList> itemInListArrayList;
    private Context context;

    public ItemInListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ItemInList> objects) {
        super(context, resource, objects);

        this.resource = resource;
        this.itemInListArrayList = objects;
        this.context = context;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ItemInListAdapter.ViewHolder viewHolder;
        if (convertView == null) {  //Nếu convertView == null thì tạo một convertView mới bằng cách infalte từ row_listview
            convertView = LayoutInflater.from(context).inflate(R.layout.item_in_list, parent, false);
            //Tạo một viewHolder để lưu các giá view lấy được từ convertView
            viewHolder = new ItemInListAdapter.ViewHolder();
            viewHolder.txt_list_food_name = (TextView) convertView.findViewById(R.id.txt_list_food_name);
            viewHolder.list_img_food = (ImageView) convertView.findViewById(R.id.list_img_food);
            viewHolder.item_food_cost = (TextView) convertView.findViewById(R.id.item_food_cost);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemInListAdapter.ViewHolder) convertView.getTag();
        }
        ItemInList itemInList = itemInListArrayList.get(position);


        viewHolder.txt_list_food_name.setText(itemInList.getName());
        viewHolder.list_img_food.setImageResource(itemInList.getImage());
        viewHolder.item_food_cost.setText(itemInList.getPrice().toString());
        return convertView;
    }

    public class ViewHolder {
        TextView txt_list_food_name, item_food_cost;
        ImageView list_img_food;
    }
}
