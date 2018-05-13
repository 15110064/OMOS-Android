package com.kt3.android.fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kt3.android.AddressActivity;
import com.kt3.android.R;
import com.kt3.android.adapter.AddressAdapter;
import com.kt3.android.domain.Address;
import com.kt3.android.other.ConstantData;
import com.kt3.android.other.MODE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class AddressFragment extends Fragment {


    RecyclerView rclvAddress;
    ArrayList<Address> listAddress;
    AddressAdapter addressAdapter;
    Gson gson;
    String access_token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_address, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), AddressActivity.class);
        intent.putExtra("MODE", MODE.ADD);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        addControls(view);
        if (access_token != null) {
            loadData();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        listAddress.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final JsonArrayRequest getAddressesReq = new JsonArrayRequest(ConstantData.ADDRESS_RESOURCE_URL + "owner",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                listAddress.add(gson.fromJson(object.toString(), Address.class));
                                Collections.sort(listAddress);
                                addressAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>(super.getHeaders());
                if (headers.containsKey("Authorization"))
                    headers.remove("Authorization");
                headers.put("Authorization", String.format("Bearer %s", access_token));
                return headers;
            }
        };

        queue.add(getAddressesReq);
    }

    private void addControls(View view) {
        rclvAddress = view.findViewById(R.id.recycler_view_address);
        listAddress = new ArrayList<>();
        addressAdapter = new AddressAdapter(getContext(), listAddress);
        rclvAddress.setAdapter(addressAdapter);

        gson = new Gson();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, Context.MODE_PRIVATE);
        access_token = sharedPreferences.getString("access_token", null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rclvAddress.setLayoutManager(layoutManager);
        rclvAddress.setNestedScrollingEnabled(false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_address);
        final AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


}
