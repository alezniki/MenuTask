package com.example.nikola.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    public final static String EMAIL = "email";
    public final static String EMAIL_VALUE = "test@testmenu.com";
    public final static String PASSWORD = "password";
    public final static String PASSWORD_VALUE = "test1234";
    public final static String URL_BASE = "https://usemenu.com/playground/public/api/v2/customer/login?app_version=2.8.1";
    public final static String SAVED_ACCESS_TOKEN = "SAVED_ACCESS_TOKEN";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etEmail = (EditText) findViewById(R.id.et_login_email);
        final EditText etPassword = (EditText) findViewById(R.id.et_login_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        final HashMap<String, String> params = new HashMap<>();
        params.put(EMAIL, EMAIL_VALUE);
        params.put(PASSWORD, PASSWORD_VALUE);

        //Request response data
        requestData(params);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validate user email and password
                boolean isValid = etEmail.getText().toString().equals(EMAIL_VALUE) && etPassword.getText().toString().equals(PASSWORD_VALUE);

                if (isValid) {
                    Intent intent = new Intent(LoginActivity.this, DetailsActivity.class);
                    startActivity(intent);
                } else {
                    //Alert user that validation is not good
                    Toast.makeText(LoginActivity.this, R.string.validation_alert, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Request JSON data
     * <p>
     * Request data via Volley POST request
     *
     * @param data map data
     */
    private void requestData(final HashMap data) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_BASE, new JSONObject(data),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        //Prevent server errors
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("############# LOGIN RESPONSE " + response);

                                try {
                                    //Store access token data from response
                                    String token = response.getString("access_token");
                                    storeTokenData(token);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {

                        //Prevent server errors
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                error.printStackTrace();
                                System.out.println("############# LOGIN ERROR " + error.toString());
                            }
                        });
                    }
                });

        //Request volley queue
        Volley.newRequestQueue(this).add(request);
    }

    /**
     * Store token data
     * <p>
     * Store token data from Json response to shared preferences
     *
     * @param data token data
     */
    private void storeTokenData(String data) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save key value set
        editor.putString(SAVED_ACCESS_TOKEN, data);
        editor.apply();
    }
}
