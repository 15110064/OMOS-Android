package com.kt3.android.other;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kt3.android.other.ConstantData.PROFILE_RESOURCE_URL;

/**
 * Created by 97lynk on 17/05/2018.
 */

public class AuthVolleyRequest {

    private Context context;
    private String access_token;

    private static AuthVolleyRequest request;

    private AuthVolleyRequest(Context context) {
        this.context = context;
        this.access_token = context.getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, Context.MODE_PRIVATE)
                .getString("access_token", null);
    }

    public static AuthVolleyRequest getInstance(Context context){
        if(request == null){
            request = new AuthVolleyRequest(context);
        }
        return request;
    }

    public void requestObject(int method, String url, JSONObject object, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Volley.newRequestQueue(context)
                .add(new JsonObjectRequest(method, url, object, listener, errorListener) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>(super.getHeaders());
                        if (headers.containsKey("Authorization"))
                            headers.remove("Authorization");
                        headers.put("Authorization", String.format("Bearer %s", access_token));
                        return headers;
                    }
                });
    }

    public void requestArray(int method, String url, JSONArray object, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        Volley.newRequestQueue(context)
                .add(new JsonArrayRequest(method, url, object, listener, errorListener) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>(super.getHeaders());
                        if (headers.containsKey("Authorization"))
                            headers.remove("Authorization");
                        headers.put("Authorization", String.format("Bearer %s", access_token));
                        return headers;
                    }
                });
    }
}
