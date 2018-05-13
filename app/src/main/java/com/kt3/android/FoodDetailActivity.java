package com.kt3.android;

import com.kt3.android.adapter.ItemInlistRecyclerViewAdapter;
import com.kt3.android.apidata.ProductApiData;
import com.kt3.android.domain.Product;
import com.kt3.android.rest.GetJsonUtils;
import com.squareup.picasso.Picasso;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class FoodDetailActivity extends AppCompatActivity {

    private RecyclerView lvItemInList;
    private RecyclerView recyclerViewMoreFood;
    private Product product;

    private ImageView detail_imgFood;
    private TextView detail_foodName;
    private TextView detail_foodPrice;
    private TextView detail_description;

    private final String CATEGORY_URI = "category";
    private final String PRODUCT_URI = "product";
    private final String PRODUCT_SEARCH_URI = PRODUCT_URI + "/search";
    private final String PRODUCT_BYCAT_URI = PRODUCT_URI + "/category/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        setUpProductRecyclerView();
        recyclerViewMoreFood = (RecyclerView) findViewById(R.id.recyclerViewMoreFood);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMoreFood.setLayoutManager(verticalLayoutManager);
        //recyclerViewMoreFood.setAdapter(ItemInListData.getItems1(this, R.layout.item_in_list));
        product = (Product) getIntent().getSerializableExtra("product");

        detail_imgFood = findViewById(R.id.detail_imgFood);
        detail_foodName = findViewById(R.id.detail_foodName);
        detail_foodPrice = findViewById(R.id.detail_foodPrice);
        detail_description = findViewById(R.id.detail_description);


        Picasso.with(this)
                //.load("http://10.0.2.2:8080" + categoryItem.getThumbnail())
                .load(product.getThumbnail())
                .resize(600, 600)
                .into(detail_imgFood);

        detail_foodName.setText(product.getName());
        detail_foodPrice.setText(product.getPrice().toString());
        detail_description.setText(product.getDescription());

        Log.i("PRODUCT", "onCreate: " + product.getCategory().getId());
        getProductsDataByCategoryFromApi(product.getCategory().getId());

    }


    /*
    Hàm này dùng để lấy thông tin Product từ Api
    Khi người dùng click vào item trong RecyclerView ở Activity trước thì ở activity này nhận được Item
    Hàm này gọi asyncTask để lấy dữ liệu từ Internet
     */
    public void getOneProductFromApi() {

        URL githubSearchUrl = GetJsonUtils.buildUrl("product", null);

        new FoodDetailActivity.ProductApiQueryTask().execute(githubSearchUrl);
    }

    private void setUpProductRecyclerView(){
        lvItemInList = (RecyclerView) findViewById(R.id.recyclerViewMoreFood);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(FoodDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        lvItemInList.setLayoutManager(verticalLayoutManager);
    }

    private void setProductRecyclerViewAdapter(ArrayList<Product> products){
        ItemInlistRecyclerViewAdapter productItemAdapter = new ItemInlistRecyclerViewAdapter(this, products);
        lvItemInList.setAdapter(productItemAdapter);
    }


    public class ProductApiQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL productGetAllUrl = params[0];
            String jsonResult = null;
            try {
                jsonResult = GetJsonUtils.getResponseFromHttpUrl(productGetAllUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String jsonResult) {
            if (jsonResult != null && !jsonResult.equals("")) {
                setProductRecyclerViewAdapter(ProductApiData.getProducts(jsonResult));
            }
        }
    }

    public void getProductsDataByCategoryFromApi(Long catId){
        URL productApiUrl = GetJsonUtils.buildUrl(PRODUCT_BYCAT_URI + String.valueOf(catId), null);
        Log.i("INFO", "getProductsDataByCategoryFromApi: " + productApiUrl.toString());
        new FoodDetailActivity.ProductApiQueryTask().execute(productApiUrl);
    }
}
