package com.kt3.android;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kt3.android.adapter.CategoryListRecyclerViewAdapter;
import com.kt3.android.adapter.ItemInlistRecyclerViewAdapter;
import com.kt3.android.apidata.CategoryApiData;
import com.kt3.android.apidata.ProductApiData;
import com.kt3.android.domain.CartItem;
import com.kt3.android.domain.Category;
import com.kt3.android.domain.Product;
import com.kt3.android.enums.ICE_LEVEL;
import com.kt3.android.enums.SUGAR_LEVEL;
import com.kt3.android.interfaces.ObserAdapterClick;
import com.kt3.android.interfaces.ViewHolderSubject;
import com.kt3.android.other.ConstantData;
import com.kt3.android.rest.GetJsonUtils;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CategoryExploreActivity extends AppCompatActivity implements ChooseProductOptionFragment.ChooseProductOptionDialogListener, ObserAdapterClick {
    private RecyclerView lvItemCategory;
    private RecyclerView lvItemInList;
    private TextView tvCategoryName;
    private final String CATEGORY_URI = "category";
    private final String PRODUCT_URI = "product";
    private final String PRODUCT_SEARCH_URI = PRODUCT_URI + "/search";
    private final String PRODUCT_BYCAT_URI = PRODUCT_URI + "/category/";
    private Product currentProduct;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCategoryDataFromApi();
        getAllProductsDataFromApi();
        setContentView(R.layout.activity_category_explore);
        setUpCategoryRecyclerView();
        setUpProductRecyclerView();

        tvCategoryName = findViewById(R.id.tvCategoryName);
        //lvItemCategory.setAdapter(ItemCategoryData.getCategoryItem1(this, R.layout.item_category_explore));
        //lvItemCategory.setAdapter(ItemCategoryData.getCategoryItem1(this, R.layout.category_item));


        //lvItemInList.setAdapter(ItemInListData.getItems1(this, R.layout.item_in_list));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);


        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setOnClickListener();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tvCategoryName.setText("Kết quả tìm kiếm");
                searchProduct(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    /*
     * Hàm này chứa các lệnh thiết lập RecyclerView hiển thị category
     */
    private void setUpCategoryRecyclerView() {
        lvItemCategory = (RecyclerView) findViewById(R.id.listChildCat);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(CategoryExploreActivity.this, LinearLayoutManager.HORIZONTAL, false);
        lvItemCategory.setLayoutManager(horizontalLayoutManager);
    }

    /*
     * Hàm này chứa các lệnh thiết lập RecycleView hiển thị Product
     */
    private void setUpProductRecyclerView() {
        lvItemInList = (RecyclerView) findViewById(R.id.lvProduct);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(CategoryExploreActivity.this, LinearLayoutManager.VERTICAL, false);
        lvItemInList.setLayoutManager(verticalLayoutManager);
    }


    /*
     * Hàm này có chức năng gán Adapter cho RecyclerView.
     * Được gọi sau khi AsyncTask hoàn thành nhiệm vụ.
     * Việc tách ra thread mới đảm bảo yêu cầu về kiến trúc và hiệu suất trong Android Application
     */
    private void setCategoryRecyclerViewAdapter(ArrayList<Category> categories) {
        CategoryListRecyclerViewAdapter categoryItemAdapter = new CategoryListRecyclerViewAdapter(this, categories);
        lvItemCategory.setAdapter(categoryItemAdapter);
    }


    /*
    Hàm này có chức năng gán Adapter cho RecyclerView Product.
    Được gọi sau khi AsyncTask hoàn thành nhiệm vụ.
    Việc tách ra thread mới đảm bảo yêu cầu về kiến trúc và hiệu suất trong Android Application
     */
    private void setProductRecyclerViewAdapter(ArrayList<Product> products) {
        ItemInlistRecyclerViewAdapter productItemAdapter = new ItemInlistRecyclerViewAdapter(this, products);
        lvItemInList.setAdapter(productItemAdapter);
    }


    public void getCategoryDataFromApi() {

        URL categoryGetAllUrl = GetJsonUtils.buildUrl(CATEGORY_URI, null);

        new CategoryApiQueryTask().execute(categoryGetAllUrl);
    }

    public void getAllProductsDataFromApi() {
        URL productGetAllUrl = GetJsonUtils.buildUrl(PRODUCT_URI, null);
        new ProductApiQueryTask().execute(productGetAllUrl);
    }

    public void searchProduct(String newText) {
        URL productSearchUrl = GetJsonUtils.buildUrl(PRODUCT_SEARCH_URI, newText);
        new ProductApiQueryTask().execute(productSearchUrl);
    }

    @Override
    public void update(long categoryId) {
        if(categoryId == 0){
            getAllProductsDataFromApi();
        }
        else {
            getProductsDataByCategoryFromApi(categoryId);
        }
    }


    public class CategoryApiQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL categoryGetAllUrl = params[0];
            String jsonResult = null;
            try {
                jsonResult = GetJsonUtils.getResponseFromHttpUrl(categoryGetAllUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String productJsonResult) {
            if (productJsonResult != null && !productJsonResult.equals("")) {
                setCategoryRecyclerViewAdapter(CategoryApiData.getCategories(productJsonResult));
            }
        }
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

    public void getProductsDataByCategoryFromApi(Long catId) {
        URL productApiUrl = GetJsonUtils.buildUrl(PRODUCT_BYCAT_URI + String.valueOf(catId), null);
        Log.i("INFO", "getProductsDataByCategoryFromApi: " + productApiUrl.toString());
        new ProductApiQueryTask().execute(productApiUrl);
    }

    public void setCategoryName(String name) {
        tvCategoryName.setText(name);
    }

    public void showChooseOptionDialog(Product product) {
        currentProduct = product;
        FragmentManager fm = getSupportFragmentManager();
        ChooseProductOptionFragment chooseProductOptionFragment = ChooseProductOptionFragment.newInstance("Some Title");
        chooseProductOptionFragment.show(fm, "chooseProductOptionFragment");
    }

    @Override
    public void onFinishChooseProductOptionDialog(SUGAR_LEVEL sugar_level, ICE_LEVEL ice_level) {
        if(sugar_level == null)
            sugar_level = SUGAR_LEVEL.ONE_HUNDRED_PERCENT;
        if(ice_level == null)
            ice_level = ICE_LEVEL.ONE_HUNDRED_PERCENT;

        access_token = getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, MODE_PRIVATE)
                .getString("access_token", null);
        if (access_token == null) finish();

        CartItem newCartItem = new CartItem();
        newCartItem.setProductId(currentProduct.getId());
        newCartItem.setIceLevel(ice_level);
        newCartItem.setSugarLevel(sugar_level);
        newCartItem.setSubTotal(new BigDecimal(2.3));
        newCartItem.setQuantity(1);
        try {
            JSONObject cartItemJson = new JSONObject(new Gson().toJson(newCartItem));
            Toast.makeText(getApplicationContext(), cartItemJson.toString(), Toast.LENGTH_SHORT).show();
            Volley.newRequestQueue(getApplicationContext())
                    .add(new JsonObjectRequest(
                            Request.Method.POST, ConstantData.ITEM_URL, cartItemJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>(super.getHeaders());
                            if (headers.containsKey("Authorization"))
                                headers.remove("Authorization");
                            headers.put("Authorization", String.format("Bearer %s", access_token));
                            return headers;
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Toast.makeText(this, "Product: " + currentProduct.getName() + " Sugar: "+ sugar_level.toString(), Toast.LENGTH_LONG).show();
    }
}
