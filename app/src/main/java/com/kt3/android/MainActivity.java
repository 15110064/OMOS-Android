package com.kt3.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kt3.android.adapter.CategoryItemAdapter;
import com.kt3.android.apidata.CategoryApiData;
import com.kt3.android.domain.Category;
import com.kt3.android.pseudodata.*;
import com.kt3.android.rest.GetJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //properties
    private RecyclerView lvItemCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Gọi hàm thiết lập recyclerview
        setUpRecyclerView();
        makeGithubSearchQuery();
        //Mapping with item_in_bill
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "888888888888888888",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


//        //Load menu category
//        recyclerMenu=(RecyclerView) findViewById(R.id.rcl_menu);
//        recyclerMenu.setHasFixedSize(true);
//        layoutManager=new GridLayoutManager(this,2);
//        recyclerMenu.setLayoutManager(layoutManager);
    }



    public void makeGithubSearchQuery() {

        URL githubSearchUrl = GetJsonUtils.buildUrl("category", null);

        new ApiQueryTask().execute(githubSearchUrl);
    }




    public class ApiQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = GetJsonUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                setLvItemCategoryAdapter(CategoryApiData.getCategories(githubSearchResults));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_menu) {
                Intent i = new Intent(this, CategoryExploreActivity.class);
                startActivity(i);

            } else if (id == R.id.nav_logout) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);

            } else if (id == R.id.nav_profile) {
                Intent i = new Intent(this, UserActivity.class);
                startActivity(i);

            } else if (id == R.id.nav_cart) {
                Intent i = new Intent(this, CartActivity.class);
                // Bill bill = new Bill("10:30 - 23/02/2018", 179000.0, "1 Võ Văn Ngân, TĐ, TPHCM", R.drawable.history1, Bill.STATUS.DANG_VC);
                //i.putExtra("bill", bill);
                startActivity(i);
                //TODO: fix error here. BillDetailActivity can't start
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Log.e("E", "onNavigationItemSelected: " + e.getMessage());
        }
        return true;
    }

    /*
    Hàm này dùng để thiết lập dữ liệu cho recyclerview
    @param: không có

     */
    private void setUpRecyclerView()
    {

        lvItemCategory = (RecyclerView) findViewById(R.id.lvItemCategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvItemCategory.setLayoutManager(layoutManager);
        lvItemCategory.setNestedScrollingEnabled(false);

    }

    private void setLvItemCategoryAdapter(ArrayList<Category> categories)
    {
        CategoryItemAdapter categoryItemAdapter = new CategoryItemAdapter(this, categories);
        lvItemCategory.setAdapter(categoryItemAdapter);
    }
}