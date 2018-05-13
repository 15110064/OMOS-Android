package com.kt3.android.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kt3.android.LoginActivity;
import com.kt3.android.R;
import com.kt3.android.domain.Account;
import com.kt3.android.domain.Profile;
import static com.kt3.android.other.ConstantData.*;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import static com.kt3.android.other.ConstantData.OAUTH2_FILE_NAME;

/**
 * Fragment chứa view thông tin (profile) khách hàng
 * Có chức năng thay đổi profile
 * Có chức năng thay đổi password
 */
public class ProfileFragment extends Fragment {
    private boolean changeInfo = false;
    private Profile profile;

    private EditText txtLastName, txtFirstName, txtEmail;
    private TextView txtFullName, txtUserName, btnChangeInfo, btnChangePassword;
    private Button btnBirthday;

    Gson gson;

    DialogInterface.OnClickListener dialogClickPosListener, dialogClickNavListener;
    SharedPreferences sharedPreferences;

    private String access_token;
    private String user_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolBarSetup(view);
        addControls(view);
        addData();
        addEvents(inflater);

        return view;
    }

    private void addEvents(final LayoutInflater inflater) {
        dialogClickPosListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // thay đổi text
                btnChangeInfo.setText(getString(R.string.change));
                // thay đổi trạng thái nút
                changeInfo = !changeInfo;

                // enable/disable các edit text
                toggleView(txtFirstName);
                toggleView(txtLastName);
                toggleView(txtEmail);
                toggleView(btnBirthday);

                // lấy các giá trị edit text vào profile
                profile.setFirstName(getText(txtFirstName));
                profile.setLastName(getText(txtLastName));
                profile.setEmailAddress(getText(txtEmail));
                profile.setBirthDay((long) btnBirthday.getTag());

                try {
                    // tạo request update
                    JSONObject profileJson = new JSONObject(gson.toJson(profile));
                    final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    JsonObjectRequest updateProfileRequest = new JsonObjectRequest(Request.Method.PUT,
                            String.format("%s%d", PROFILE_RESOURCE_URL, profile.getId()), profileJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response != null && "success".equals(response.getString("message"))) {
                                            Toast.makeText(getActivity(), "Thông tin đã được cập nhật", Toast.LENGTH_LONG).show();
                                            addData();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.i("VOLLEY ERROR", "onErrorResponse: " + error.getMessage());
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
                    };
                    requestQueue.add(updateProfileRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        dialogClickNavListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // thay đổi text
                btnChangeInfo.setText(getString(R.string.change));
                // thay đổi trạng thái nút
                changeInfo = !changeInfo;

                // enable/disable các edit text
                toggleView(txtFirstName);
                toggleView(txtLastName);
                toggleView(txtEmail);
                toggleView(btnBirthday);

                // đưa thông tin profile vào lại edit text
                setText(txtFirstName, profile.getFirstName());
                setText(txtLastName, profile.getLastName());
                setText(txtEmail, profile.getEmailAddress());
                btnBirthday.setText(profile.getBirthDayString());

            }
        };

        btnBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tạo calender
                final Calendar calendar = Calendar.getInstance();
                // set ngày sinh
                calendar.setTime(new Date(profile.getBirthDay()));

                // tạo datetime dialog  vói cal trên
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                btnBirthday.setTag(calendar.getTime().getTime());

                                SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_fomat));
                                btnBirthday.setText(format.format(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle(getString(R.string.change_birthday));
                datePickerDialog.show();
            }
        });

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!changeInfo) {
                        // thay đổi text
                        btnChangeInfo.setText(getString(R.string.save));
                        // thay đổi trạng thái nút
                        changeInfo = true;

                        // enable/disable các edit text
                        toggleView(txtFirstName);
                        toggleView(txtLastName);
                        toggleView(txtEmail);
                        toggleView(btnBirthday);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.save_change)
                                .setPositiveButton(R.string.ok, dialogClickPosListener)
                                .setNegativeButton(R.string.cancel, dialogClickNavListener)
                                .show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = inflater.inflate(R.layout.layout_change_password, null);

                final EditText txtOldPassword = view.findViewById(R.id.txtOldPassword);
                final EditText txtNewPassword = view.findViewById(R.id.txtNewPassword);
                final EditText txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);

                final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(view).setTitle(getString(R.string.change_password))
                        .setPositiveButton(R.string.save, null)
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                dialog.show();

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setText(getString(R.string.save));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // valid các trường
                        String newPassword = txtNewPassword.getText().toString();
                        String oldPassword = txtOldPassword.getText().toString();

                        txtNewPassword.setError(null);
                        txtOldPassword.setError(null);
                        txtConfirmPassword.setError(null);

                        if (oldPassword.isEmpty())
                            txtOldPassword.setError("Không để trống");

                        if (newPassword.isEmpty())
                            txtNewPassword.setError("Không để trống");

                        if (newPassword.contains(" "))
                            txtNewPassword.setError("Mật khẩu không được chứa khoảng trắng");

                        if (newPassword.length() < 3)
                            txtNewPassword.setError("Mật khẩu tối thiểu 3 kí tự");

                        if (!newPassword.equals(txtConfirmPassword.getText().toString())) {
                            txtConfirmPassword.setError("Mật khẩu không khớp");
                        }
                        // kiểm tra password cũ
                        if (!oldPassword.equals(sharedPreferences.getString("password", ""))) {
                            txtOldPassword.setError("Mật khẩu hiện tại không đúng");
                        }

                        if (newPassword.equals(oldPassword)) {
                            txtNewPassword.setError("Mật khẩu mới không được giống mật khẩu hiện tại");
                        }

                        if (txtOldPassword.getError() != null || txtNewPassword.getError() != null
                                || txtConfirmPassword.getError() != null)
                            return;

                        Account account = new Account();
                        account.setId(sharedPreferences.getInt("account_id", -1));
                        account.setUserName(sharedPreferences.getString("user_name", ""));
                        account.setPassword(newPassword);
                        account.setEnabled(sharedPreferences.getBoolean("account_enable", true));
                        try {
                            JSONObject accountJson = new JSONObject(gson.toJson(account));

                            RequestQueue queue = Volley.newRequestQueue(getActivity());
                            JsonObjectRequest changPasswordReq = new JsonObjectRequest(Request.Method.PUT,
                                    String.format("%s%d", ACCOUNT_RESOURCE_URL, account.getId()),
                                    accountJson,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (response != null && "success".equals(response.getString("message"))) {
                                                    Toast.makeText(getActivity(), "Mật khẩu thay đổi thành công! Vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
                                                    sharedPreferences.edit().clear();
                                                    getActivity().finish();

                                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                    getActivity().startActivity(intent);
                                                    dialog.dismiss();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("PASS", "onErrorResponse: " + error.getMessage());
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
                            };
                            queue.add(changPasswordReq);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

//                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });


            }
        });
    }

    private void addData() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest getProfile = new JsonObjectRequest(
                String.format("%sowner", PROFILE_RESOURCE_URL), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       profile = gson.fromJson(response.toString(), Profile.class);

                        txtFullName.setText(String.format("%s %s", profile.getFirstName(), profile.getLastName()));
                        txtFirstName.setText(profile.getFirstName());
                        txtLastName.setText(profile.getLastName());
                        txtEmail.setText(profile.getEmailAddress());
                        btnBirthday.setText(profile.getBirthDayString());
                        btnBirthday.setTag(profile.getBirthDay());
                    }
                }, new Response.ErrorListener() {
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
        };

        requestQueue.add(getProfile);

    }

    private void addControls(View view) {
        sharedPreferences = getActivity().getSharedPreferences(OAUTH2_FILE_NAME, Context.MODE_PRIVATE);

        txtFullName = view.findViewById(R.id.txtFullName);
        txtUserName = view.findViewById(R.id.txtUserName);

        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnBirthday = view.findViewById(R.id.btnBirthday);

        btnChangeInfo = view.findViewById(R.id.btnChangeInfo);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);

        gson = new Gson();

        access_token = sharedPreferences.getString("access_token", null);
        user_name = sharedPreferences.getString("user_name", "Empty");

        if (access_token == null) getActivity().finish();
        txtUserName.setText(user_name);
    }

    private void toolBarSetup(View view) {
        // toolbar setup
        Toolbar toolbar = view.findViewById(R.id.tb_info);
        final AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    // dựa vào changeInfo để enable/ disable các view
    public void toggleView(View view) {
        view.setEnabled(changeInfo);

        if (changeInfo) {
            view.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            view.setBackgroundTintList(getContext().getResources()
                    .getColorStateList(R.color.colorAccent));
        } else {
            view.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            view.setBackgroundTintList(getContext().getResources()
                    .getColorStateList(android.R.color.transparent));
        }
    }

    // get text từ edit text
    private String getText(EditText view) {
        return view.getText().toString();
    }

    // set text cho edit text
    private void setText(EditText view, String text) {
        view.setText(text);
    }
}
