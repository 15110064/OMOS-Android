package com.kt3.android.rest;


import android.net.Uri;

import com.kt3.android.utils.WebServiceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
/**
 * Created by khoa1 on 4/15/2018.
 */

/*
 *Lớp này chứa các phương thức hỗ trợ việc lấy dữ liệu dạng JSON từ WebAPI
 */
public class GetJsonUtils {
    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";

    /**
     * Xây dựng URL để lấy dữ liệu
     *
     * @param searchQuery keyword cần để query, trường hợp không có thì truyền vào null
     * @return The URL được sử dụng để query
     */
    public static URL buildUrl(String URI, String searchQuery) {
        Uri builtUri = Uri.parse(WebServiceUtils.DOMAIN +":"+ WebServiceUtils.PORT + "/" + URI).buildUpon()
                .appendQueryParameter(WebServiceUtils.PARAM_QUERY, searchQuery)
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

    /**
     * Đọc response trả về từ URL
     *
     * @param url url được xây dựng, có kiểu là URL
     * @return Chuỗi string là body của response
     */
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
