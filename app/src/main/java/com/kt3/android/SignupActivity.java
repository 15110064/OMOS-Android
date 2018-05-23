package com.kt3.android;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kt3.android.domain.Account;
import com.kt3.android.domain.Profile;
import com.kt3.android.other.ConstantData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class SignupActivity extends AppCompatActivity {

    private FancyButton btnSignup, btnlogin;

    private EditText txtUsername, txtPassword, txtReEnterPassword,
            txtFirstName, txtLastName, txtEmailAddress, txtBirthday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        addControls();
        addEvents();
    }

    private void addEvents() {
        final Calendar calendar = Calendar.getInstance();
        txtBirthday.setTag(calendar.getTime());
        txtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                txtBirthday.setText(format.format(cal.getTime()));
                                txtBirthday.setTag(cal.getTime());
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // valid các trường
                String password = txtPassword.getText().toString();
                txtPassword.setError(null);
                txtReEnterPassword.setError(null);

                if (password.length() < 3)
                    txtPassword.setError("Tối thiểu 3 kí tự");

                if (!password.equals(txtReEnterPassword.getText().toString()))
                    txtReEnterPassword.setError("Mật khẩu không khớp");

                if (txtFirstName.getText().toString().trim().isEmpty())
                    txtFirstName.setError("Không để trống");

                if (txtLastName.getText().toString().trim().isEmpty())
                    txtLastName.setError("Không để trống");

                if (txtPassword.getError() != null ||
                        txtReEnterPassword.getError() != null ||
                        txtFirstName.getError() != null ||
                        txtLastName.getError() != null)
                    return;

                Profile profile = new Profile();
                profile.setFirstName(txtFirstName.getText().toString());
                profile.setLastName(txtLastName.getText().toString());
                profile.setEmailAddress(txtEmailAddress.getText().toString());
                profile.setBirthDay(((Date) txtBirthday.getTag()).getTime());

                Account newAccount = new Account();
                newAccount.setUserName(txtUsername.getText().toString());
                newAccount.setUserName(txtUsername.getText().toString());
                newAccount.setEnabled(false);
                newAccount.setProfile(profile);
                try {
                    Gson gson = new Gson();
                    JSONObject accountJson = new JSONObject(gson.toJson(newAccount));
                    Volley.newRequestQueue(SignupActivity.this).add(new JsonObjectRequest(Request.Method.POST, ConstantData.REGISTRATION_URL, accountJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");
                                        String message = response.getString("message");
                                        if(status.contains("success")){
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                        }
                                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private void addControls() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtReEnterPassword = findViewById(R.id.txtReEnterPassword);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtBirthday = findViewById(R.id.txtBirthday);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);

        btnSignup = findViewById(R.id.btnSignup);
        btnlogin = findViewById(R.id.btnlogin);

    }
}
