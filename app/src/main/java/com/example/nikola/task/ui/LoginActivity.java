package com.example.nikola.task.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.example.nikola.task.R;
import com.example.nikola.task.utils.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.nikola.task.utils.Constants.EMAIL;
import static com.example.nikola.task.utils.Constants.EMAIL_VALUE;
import static com.example.nikola.task.utils.Constants.PASSWORD;
import static com.example.nikola.task.utils.Constants.PASSWORD_VALUE;
import static com.example.nikola.task.utils.Constants.SHARED_PREFS;
import static com.example.nikola.task.utils.Constants.TOKEN_KEY;
import static com.example.nikola.task.utils.Constants.URL_BASE_LOGIN;

public class LoginActivity extends AppCompatActivity {

    /**
     * Login manager
     */
    private LoginManager loginManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_asset));

        loginManager = new LoginManager(getApplicationContext());

        final EditText etEmail = (EditText) findViewById(R.id.et_login_email);
        final EditText etPassword = (EditText) findViewById(R.id.et_login_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        //Set type face
        etEmail.setTypeface(typeface);
        etPassword.setTypeface(typeface);
        btnLogin.setTypeface(typeface);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validate user email and password
                boolean isValid = etEmail.getText().toString().equals(EMAIL_VALUE) && etPassword.getText().toString().equals(PASSWORD_VALUE);

                if (isValid) {
                    loginManager.setLoggedIn(true);
                } else {
                    loginManager.setLoggedIn(false);
                    //Alert user that validation is not good
                    Toast.makeText(LoginActivity.this, R.string.validation_alert, Toast.LENGTH_SHORT).show();
                }

                if (loginManager.isLoggedIn()) {
                    //Go to details screen after login is successful
                    startActivity(new Intent(LoginActivity.this, DetailsActivity.class));

                    // Destroy activity
                    finish();
                }
            }
        });

        //Request response data
        requestData();
    }

    /**
     * Request JSON data
     * <p>
     * Request data via Volley POST request
     */
    private void requestData() {

        HashMap<String, String> params = new HashMap<>();
        params.put(EMAIL, EMAIL_VALUE);
        params.put(PASSWORD, PASSWORD_VALUE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_BASE_LOGIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        //Prevent server errors
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("############# LOGIN SCREEN VOLLEY RESPONSE: " + response);

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
                                System.out.println("############# LOGIN SCREEN VOLLEY ERROR: " + error.toString());
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
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TOKEN_KEY, data);
        editor.apply();
    }
}
