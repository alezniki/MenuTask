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

    public final static String EMAIL_KEY = "email";
    public final static String EMAIL_VALUE = "test@testmenu.com";
    public final static String PASSWORD_KEY = "password";
    public final static String PASSWORD_VALUE = "test1234";
    public final static String URL_BASE = "https://usemenu.com/playground/public/api/v2/customer/login?app_version=2.8.1";

    /**
     * Is login valid
     */
    private boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set references
        final EditText etEmail = (EditText) findViewById(R.id.et_login_email);
        final EditText etPassword = (EditText) findViewById(R.id.et_login_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        HashMap<String, String> params = new HashMap<>();
        params.put(EMAIL_KEY, EMAIL_VALUE);
        params.put(PASSWORD_KEY, PASSWORD_VALUE);

        //Request response data
        requestData(params);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set login validation
                isValid = etEmail.getText().toString().equals(EMAIL_VALUE) && etPassword.getText().toString().equals(PASSWORD_VALUE);

                if (isValid) {
                    //Todo Pass the intent to detail activity with stored access token
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
     * Request Json data
     * <p>
     * Request data via Volley POST request
     *
     * @param data data
     */
    private void requestData(final HashMap data) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_BASE, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        
                        //Prevent server errors
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("############# JSON RESPONSE " + response);

                                try {
                                    String token = response.getString("access_token");
                                    System.out.println("############# ACCESS TOKEN " + token);

                                    //Store access token response
                                    storeResponse(token);
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
                                System.out.println("############# ERROR " + error.toString());
                            }
                        });
                    }
                });

        //Request volley queue
        Volley.newRequestQueue(this).add(request);
    }

    /**
     * Store response
     * <p>
     * Store access token from Json response to shared preferences
     *
     * @param token access token
     */
    private void storeResponse(String token) {

        //Get default preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("SAVED_ACCESS_TOKEN", token);
        editor.apply();

        System.out.println("############# STORED TOKEN " + token);
    }
}
