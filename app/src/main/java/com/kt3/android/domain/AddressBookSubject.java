package com.kt3.android.domain;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kt3.android.other.ConstantData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by 97lynk on 14/05/2018.
 */

public class AddressBookSubject extends Observable {

    private ArrayList<Address> addressArrayList;
    private static AddressBookSubject OBJECT;
    private Context context;

    private AddressBookSubject(Context context) {
        this.context = context;
        addressArrayList = new ArrayList<>();
    }

    public static AddressBookSubject getIntance(Context context) {
        if (OBJECT == null)
            OBJECT = new AddressBookSubject(context);
        return OBJECT;
    }

    public ArrayList<Address> getAddressArrayList() {
        return addressArrayList;
    }

    public void setAddressArrayList(ArrayList<Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }


    @Override
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg);
    }

    public void loadData() {
        final String access_token = context.getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, Context.MODE_PRIVATE)
                .getString("access_token", null);
        addressArrayList.clear();
        Volley.newRequestQueue(context).add(new JsonArrayRequest(ConstantData.ADDRESS_RESOURCE_URL + "owner",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                addressArrayList.add(gson.fromJson(object.toString(), Address.class));
                                Collections.sort(addressArrayList);
                            }
                            Log.i("I", "onResponse: load data");
                            setChanged();
                            notifyObservers(new Address());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        });

//        super.notifyObservers();
    }
}
