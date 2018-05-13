package com.kt3.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt3.android.domain.Account;

import java.util.HashMap;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthError;
import ca.mimic.oauth2library.OAuthResponse;

import static com.kt3.android.other.ConstantData.CLIENT_ID;
import static com.kt3.android.other.ConstantData.CLIENT_SECRET;
import static com.kt3.android.other.ConstantData.OAUTH2_FILE_NAME;
import static com.kt3.android.other.ConstantData.OWNER_RESOURCE_PASSWORD_GRANT;
import static com.kt3.android.other.ConstantData.REQUEST_TOKEN_URL;

public class LoginActivity extends AppCompatActivity {
    private TextView link_signup;
    private Button btn_login;
    private EditText txtUsername, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        link_signup = findViewById(R.id.link_signup);
        btn_login = findViewById(R.id.btn_login);
        txtUsername = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);


        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncLogin login = new AsyncLogin();
                login.execute(txtUsername.getText().toString(), txtPassword.getText().toString());
            }
        });
    }

    private class AsyncLogin extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            // tạo request
            OAuth2Client client = new OAuth2Client.Builder(CLIENT_ID, CLIENT_SECRET, REQUEST_TOKEN_URL)
                    .username(strings[0]).password(strings[1])
                    .grantType(OWNER_RESOURCE_PASSWORD_GRANT).build();

            try {
                // lấy response
                OAuthResponse response = client.requestAccessToken();
                if (response.isSuccessful()) { // đăng nhập thành công
                    // tạo gson parser
                    Gson gson = new Gson();
                    // parse body response zô hashmap
                    HashMap<String, Object> jsonProperties = gson.fromJson(response.getBody(), HashMap.class);
                    // parse properties account
                    Account accountRes = gson.fromJson(jsonProperties.get("account").toString(), Account.class);

                    // Lưu các thông tin
                    SharedPreferences.Editor editor =  getSharedPreferences(OAUTH2_FILE_NAME, MODE_PRIVATE).edit();
                    editor.clear();
                    editor.putInt("account_id", accountRes.getId());
                    editor.putBoolean("account_enable", accountRes.isEnabled());
                    editor.putString("user_name", strings[0]);
                    editor.putString("password", strings[1]);
                    editor.putString("access_token", response.getAccessToken());
                    editor.commit();
                    publishProgress("You are logging success !!! ");
                    return true;
                } else { // đăng nhập không thành công
                    OAuthError error = response.getOAuthError();
                    publishProgress(String.format("%s", error.getErrorDescription()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                publishProgress(String.format("Exception: %s", ex.getMessage()));
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(LoginActivity.this, values[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
