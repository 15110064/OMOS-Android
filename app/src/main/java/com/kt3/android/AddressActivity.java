package com.kt3.android;

import com.android.volley.AuthFailureError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kt3.android.domain.Address;
import com.kt3.android.domain.Location;
import com.kt3.android.other.ConstantData;
import com.kt3.android.other.LOCATION_TYPE;
import com.kt3.android.other.MODE;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static com.kt3.android.other.ConstantData.ADDRESS_RESOURCE_URL;

public class AddressActivity extends AppCompatActivity {

    private MODE mode;
    private Button btnSaveAddress;
    private EditText txtFullName, txtPhone, txtAddress;
    private MaterialSpinner spCity, spProvince, spWard;

    private ArrayList<Location> listCities, listProvince, listWard;
    private ArrayAdapter cityAdapter, provinceAdapter, wardAdapter;

    private Address address;
    private String access_token;

    View.OnClickListener addAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!valid()) return;

            try {
                Location city = (Location) spCity.getSelectedItem();
                Location province = (Location) spProvince.getSelectedItem();
                Location ward = (Location) spWard.getSelectedItem();

                Address newAddress = new Address();
                newAddress.setFullName(txtFullName.getText().toString());
                newAddress.setPhone(txtPhone.getText().toString());
                newAddress.setAddress(txtAddress.getText().toString());
                newAddress.setCityID(city.getKey());
                newAddress.setCityName(city.getValue());
                newAddress.setProvinceID(province.getKey());
                newAddress.setProvinceName(province.getValue());
                newAddress.setWardID(ward.getKey());
                newAddress.setWardName(ward.getValue());


                JSONObject addressJson = new JSONObject(new Gson().toJson(newAddress));

                Volley.newRequestQueue(getApplicationContext()).add(new JsonObjectRequest
                        (Request.Method.POST, ADDRESS_RESOURCE_URL,
                                addressJson, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.toString().contains("success")) {
                                    Toast.makeText(getApplicationContext(), "Đã thêm địa chỉ", Toast.LENGTH_LONG).show();
                                    finish();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener editAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!valid()) return;

            try {
                Location city = (Location) spCity.getSelectedItem();
                Location province = (Location) spProvince.getSelectedItem();
                Location ward = (Location) spWard.getSelectedItem();

                Address newAddress = new Address();
                newAddress.setFullName(txtFullName.getText().toString());
                newAddress.setPhone(txtPhone.getText().toString());
                newAddress.setAddress(txtAddress.getText().toString());
                newAddress.setCityID(city.getKey());
                newAddress.setCityName(city.getValue());
                newAddress.setProvinceID(province.getKey());
                newAddress.setProvinceName(province.getValue());
                newAddress.setWardID(ward.getKey());
                newAddress.setWardName(ward.getValue());


                JSONObject addressJson = new JSONObject(new Gson().toJson(newAddress));

                Volley.newRequestQueue(getApplicationContext()).add(new JsonObjectRequest
                        (Request.Method.PUT, ADDRESS_RESOURCE_URL + address.getId(),
                                addressJson, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.toString().contains("success")) {
                                    Toast.makeText(getApplicationContext(), "Đã sửa địa chỉ", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        access_token = getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, MODE_PRIVATE)
                .getString("access_token", "");

        addControls();
        addEvents();
    }

    private void addEvents() {
        listProvince.clear();
        provinceAdapter.notifyDataSetChanged();

        spProvince.setEnabled(false);
        spWard.setEnabled(false);

        Volley.newRequestQueue(getApplicationContext()).add(new JsonObjectRequest
                ("http://prod.boxme.vn/api/public/api/merchant/rest/lading/province/" + listCities.get(0).getKey(),
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray provinces = response.getJSONArray("data");
                            for (int i = 0; i < provinces.length(); i++) {
                                JSONObject province = provinces.getJSONObject(i);
                                int provinceId = province.getInt("ProvinceId");
                                String provinceName = province.getString("ProvinceName");
                                if ((Arrays.asList(568, 558, 571).contains(provinceId)))
                                    listProvince.add(new Location(
                                            provinceId, provinceName));
                            }
                            provinceAdapter.notifyDataSetChanged();
                            spProvince.setEnabled(true);
                            if (mode == MODE.EDIT) {
                                spProvince.setSelection(findIndexLocationById(listProvince, address.getProvinceID()) + 1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                }));


        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listWard.clear();
                wardAdapter.notifyDataSetChanged();
                spWard.setEnabled(false);

                if (position < 0 || position >= listProvince.size()
                        || listProvince.get(position) == null) {
                    spWard.setSelection(0);
                } else {
                    Volley.newRequestQueue(getApplicationContext()).add(new JsonObjectRequest
                            ("http://prod.boxme.vn/api/public/api/merchant/rest/lading/ward/" + listProvince.get(position).getKey(), null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                JSONArray wards = response.getJSONArray("data");
                                                for (int i = 0; i < wards.length(); i++) {
                                                    JSONObject province = wards.getJSONObject(i);
                                                    listWard.add(new Location(
                                                            province.getInt("WardId"), province.getString("WardName")));
                                                }

                                                wardAdapter.notifyDataSetChanged();
                                                spWard.setEnabled(true);

                                                if (mode == MODE.EDIT) {
                                                    spWard.setSelection(findIndexLocationById(listWard, address.getWardID()) + 1);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub

                                }
                            }));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mode == MODE.ADD) {
            btnSaveAddress.setText("Thêm địa chỉ");
            btnSaveAddress.setOnClickListener(addAddressListener);
        } else if (mode == MODE.EDIT) {
            btnSaveAddress.setText("Sửa địa chỉ");
            address = (Address) getIntent().getSerializableExtra("ADDRESS");
            txtFullName.setText(address.getFullName());
            txtPhone.setText(address.getPhone());
            txtAddress.setText(address.getAddress());

            btnSaveAddress.setOnClickListener(editAddressListener);
        }
    }

    private void addControls() {
        mode = (MODE) getIntent().getSerializableExtra("MODE");

        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        txtFullName = findViewById(R.id.txtFullName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);

        spCity = findViewById(R.id.spCity);
        spProvince = findViewById(R.id.spProvince);
        spWard = findViewById(R.id.spWard);

        listCities = new ArrayList<>();
        listProvince = new ArrayList<>();
        listWard = new ArrayList<>();

        listCities.add(new Location(52, "TP.Hồ Chí Minh"));
        spCity.setSelection(1);
        cityAdapter = new ArrayAdapter(getApplicationContext(), R.layout.item_in_location, listCities);

        spCity.setAdapter(cityAdapter);
        spCity.setEnabled(false);

        provinceAdapter = new ArrayAdapter(getApplicationContext(), R.layout.item_in_location, listProvince);
        provinceAdapter.setDropDownViewResource(R.layout.item_in_location);
        spProvince.setAdapter(provinceAdapter);

        wardAdapter = new ArrayAdapter(getApplicationContext(), R.layout.item_in_location, listWard);
        wardAdapter.setDropDownViewResource(R.layout.item_in_location);
        spWard.setAdapter(wardAdapter);

    }

    private int findIndexLocationById(List<Location> locationList, int id) {
        for (int i = 0; i < locationList.size(); i++) {
            if (locationList.get(i).getKey() == id)
                return i;
        }
        return -1;
    }

    private boolean valid() {
        txtFullName.setError(null);
        txtPhone.setError(null);
        txtAddress.setError(null);
        spProvince.setError(null);
        spWard.setError(null);

        boolean isValid = true;

        if (isEmty(txtFullName)) {
            txtFullName.setError("Không để trống");
            isValid = false;
        }
        if (isEmty(txtPhone)) {
            txtPhone.setError("Không để trống");
            isValid = false;
        }
        if (isEmty(txtAddress)) {
            txtAddress.setError("Không để trống");
            isValid = false;
        }
        if (spProvince.getSelectedItemPosition() <= 0) {
            spProvince.setError("Vui lòng chọn");
            isValid = false;
        }
        if (spWard.getSelectedItemPosition() <= 0) {
            spWard.setError("Vui lòng chọn");
            isValid = false;
        }
        return isValid;
    }

    private boolean isEmty(EditText editText) {
        return (editText.getText() == null ||
                editText.getText().toString().length() <= 0);
    }
}
