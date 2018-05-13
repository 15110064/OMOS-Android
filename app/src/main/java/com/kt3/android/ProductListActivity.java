package com.kt3.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.kt3.android.pseudodata.*;
public class ProductListActivity extends AppCompatActivity {
    //properties
    private ListView lvItemInList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        lvItemInList = (ListView) findViewById(R.id.lvProduct);
        lvItemInList.setAdapter(ItemInListData.getItems(this, R.layout.item_in_list));
        lvItemInList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductListActivity.this, FoodDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
