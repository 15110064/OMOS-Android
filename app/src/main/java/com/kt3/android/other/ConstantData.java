package com.kt3.android.other;

/**
 * Created by 97lynk on 09/04/2018.
 */

public class ConstantData {
    public static final String OAUTH2_FILE_NAME = "kt3_oauth2";
    //    public static final String REQUEST_TOKEN_URL = "https://oauth2-service.azurewebsites.net/oauth/token";
    private static final String LOCAL_HOST = "http://192.168.1.9";
    public static final String REQUEST_TOKEN_URL = LOCAL_HOST + ":8888/oauth/token";
    public static final String CLIENT_ID = "android";
    public static final String CLIENT_SECRET = "android";
    public static final String OWNER_RESOURCE_PASSWORD_GRANT = "password";

//    public static final String PROFILE_RESOURCE_URL = "https://account-service-kt3.azurewebsites.net/profile/";
//    public static final String ACCOUNT_RESOURCE_URL = "https://account-service-kt3.azurewebsites.net/account/";
//    public static final String ADDRESS_RESOURCE_URL = "https://address-service-kt3.azurewebsites.net/address/";
//    public static final String REGISTRATION_URL = "https://account-service-kt3.azurewebsites.net/account/registration";
//    public static final String CART_URL = "https://order-service-kt3.azurewebsites.net/cart/";
//    public static final String ITEM_URL = "https://order-service-kt3.azurewebsites.net/cart/item/";

    public static final String PROFILE_RESOURCE_URL = LOCAL_HOST + ":8081/profile/";
    public static final String ACCOUNT_RESOURCE_URL = LOCAL_HOST + ":8081/account/";
    public static final String ADDRESS_RESOURCE_URL = LOCAL_HOST + ":8082/address/";
    public static final String REGISTRATION_URL = LOCAL_HOST + ":8081/account/registration";
    public static final String CART_URL = LOCAL_HOST + ":8084/cart/";
    public static final String ITEM_URL = LOCAL_HOST + ":8084/cart/item/";
    public static final String ORDER_URL = LOCAL_HOST + ":8084/order/";
}
