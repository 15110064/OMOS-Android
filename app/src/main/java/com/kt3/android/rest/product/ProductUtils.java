package com.kt3.android.rest.product;

import android.net.Uri;

import com.kt3.android.utils.WebServiceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/*
Lớp này chứa các hàm hỗ trợ việc Query dữ liệu từ Api Service
 */
public class ProductUtils {


//    final static String DOMAIN =
//            "http://192.168.137.1";
//    final static String PORT = "8080";

    final static String PARAM_QUERY = "s";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";

    /**
     * Builds the URL used to query GitHub.
     *
     * @param githubSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the GitHub.
     */
    public static URL buildUrl(String githubSearchQuery) {
        Uri builtUri = Uri.parse(WebServiceUtils.DOMAIN +":"+ WebServiceUtils.PORT + "/product").buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

//    public static URL buildUrlGetByCategory(String githubSearchQuery) {
//        Uri builtUri = Uri.parse(DOMAIN +":"+ PORT + "/product/category/" + githubSearchQuery).buildUpon()
//                .appendQueryParameter(PARAM_SORT, sortBy)
//                .build();
//
//        URL url = null;
//        try {
//            url = new URL(builtUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        Log.i("get product by cat", "buildUrlGetByCategory: "+ builtUri.toString());
//        return url;
//    }

//
//    public static URL buildUrlSearch(String githubSearchQuery) {
//        Uri builtUri = Uri.parse(DOMAIN +":"+ PORT + "/product/?s=" + githubSearchQuery).buildUpon()
//                .appendQueryParameter(PARAM_SORT, sortBy)
//                .build();
//
//        URL url = null;
//        try {
//            url = new URL(builtUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        return url;
//    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
