package com.kt3.android.domain;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kt3.android.other.AuthVolleyRequest;
import com.kt3.android.other.ConstantData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
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

    public static AddressBookSubject getInstance(Context context) {
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
        addressArrayList.clear();
        AuthVolleyRequest.getInstance(context).
                requestArray(Request.Method.GET, ConstantData.ADDRESS_RESOURCE_URL + "owner", null,
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
                        });
    }
}
