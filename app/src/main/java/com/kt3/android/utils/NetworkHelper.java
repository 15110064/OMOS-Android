package com.kt3.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by khoa1 on 4/6/2018.
 */

public class NetworkHelper {

    public static boolean hasNetworkAccess(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return (activeNetwork.isConnectedOrConnecting() && activeNetwork != null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
