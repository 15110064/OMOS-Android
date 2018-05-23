package com.kt3.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.goodiebag.pinview.Pinview;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kt3.android.adapter.CartItemAdapter;
import com.kt3.android.domain.Address;
import com.kt3.android.domain.AddressBookSubject;
import com.kt3.android.domain.Cart;
import com.kt3.android.domain.CartItem;
import com.kt3.android.domain.CartSubject;
import com.kt3.android.other.AuthVolleyRequest;
import com.kt3.android.other.ConstantData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.zip.Inflater;

public class CartActivity extends AppCompatActivity implements Observer {

    private TextView txtGrandTotal;
    private RecyclerView rclvCart;
    private ArrayList<CartItem> cartItems;
    private CartItemAdapter cartItemAdapter;
    private String access_token;
    private CartSubject subject;

    private Spinner spAddress;
    private ArrayAdapter<Address> adapterAddress;

    private Button btnSubmit;
    private AddressBookSubject addressBookSubject;

    private AlertDialog submitCodeDialog;
    private Pinview pinvCode;
    private TextView txtDescription;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        subject = CartSubject.getInstance();
        subject.addObserver(this);

        addressBookSubject = AddressBookSubject.getInstance(getApplicationContext());
        addressBookSubject.addObserver(this);
        addControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (access_token != null) {
            loadData();
            addressBookSubject.loadData();
        }
    }

    private void addControls() {
        txtGrandTotal = findViewById(R.id.txtGrandTotal);
        rclvCart = findViewById(R.id.rclvCart);
        cartItems = new ArrayList<>();
        cartItemAdapter = new CartItemAdapter(CartActivity.this, cartItems);
        rclvCart.setAdapter(cartItemAdapter);

        access_token = getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, MODE_PRIVATE)
                .getString("access_token", null);
        if (access_token == null) finish();

        adapterAddress = new ArrayAdapter(this, android.R.layout.simple_spinner_item, addressBookSubject.getAddressArrayList());
        adapterAddress.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        View view = getLayoutInflater().inflate(R.layout.layout_choose_address, null);

        spAddress = view.findViewById(R.id.spAddress);
        spAddress.setAdapter(adapterAddress);

        final AlertDialog dialog = new AlertDialog.Builder(CartActivity.this)
                .setView(view).setTitle("Chọn địa chỉ")
                .setPositiveButton("Tiếp tục", chooseAddress)
                .setNegativeButton(R.string.cancel, null)
                .create();
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        view = getLayoutInflater().inflate(R.layout.layout_submit_code, null);
        pinvCode = view.findViewById(R.id.pivCode);
        txtDescription = view.findViewById(R.id.txtDescription);
        pinvCode.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {
                AuthVolleyRequest.getInstance(getApplicationContext())
                        .requestObject(Request.Method.POST, ConstantData.CART_URL + "/submit?code=" + pinview.getValue(), null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Toast.makeText(CartActivity.this, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                                            String status = response.getString("status");
                                            if (status.contains("success"))
                                                submitCodeDialog.cancel();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
            }
        });
        submitCodeDialog = new AlertDialog.Builder(CartActivity.this)
                .setView(view).setTitle("Nhập mã xác nhận")
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    DialogInterface.OnClickListener chooseAddress = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(CartActivity.this, ((Address) spAddress.getSelectedItem()).getFullName(),
                    Toast.LENGTH_LONG).show();
            Address address = (Address) spAddress.getSelectedItem();
            AuthVolleyRequest.getInstance(getApplicationContext())
                    .requestObject(Request.Method.POST, ConstantData.CART_URL + "?addressId=" + address.getId(), null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Toast.makeText(CartActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    loadData();
                                    submitCodeDialog.show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
        }
    };

    public void loadData() {
        cartItems.clear();
        cartItemAdapter.notifyDataSetChanged();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Volley.newRequestQueue(getApplicationContext()).add(new JsonObjectRequest(
                ConstantData.CART_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            subject.setCart(gson.fromJson(response.toString(), Cart.class));
//                            txtGrandTotal.setText(String.format("%s đ", subject.getCart().getTotalPrice()));

                            cartItems.addAll(subject.getCartItems());
                            subject.setCartItems(cartItems);
//                            cartItemAdapter.notifyDataSetChanged();
                            subject.calculateTotalPrice();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartActivity.this,
                                error.toString(), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clear_all_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this)
                .setMessage("Xóa hết luôn hả?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearAllCartItems();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
        return super.onOptionsItemSelected(item);
    }

    private void clearAllCartItems() {
        Volley.newRequestQueue(getApplicationContext()).add(new JsonObjectRequest(
                Request.Method.DELETE, ConstantData.ITEM_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadData();
                        Toast.makeText(CartActivity.this,
                                response.toString(), Toast.LENGTH_SHORT).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartActivity.this,
                                error.toString(), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.i("I", "update: activity");
        if (arg instanceof Address)
            adapterAddress.notifyDataSetChanged();
        else {
            if (subject.getCartItems().isEmpty()) {
                closeOptionsMenu();
                btnSubmit.setEnabled(false);
            } else {
                openOptionsMenu();
                btnSubmit.setEnabled(true);
            }
            txtGrandTotal.setText(String.format("%s", subject.getCart().getTotalPrice()));
        }
    }

    @Override
    protected void onDestroy() {
        subject.deleteObserver(this);
        super.onDestroy();
    }
}
