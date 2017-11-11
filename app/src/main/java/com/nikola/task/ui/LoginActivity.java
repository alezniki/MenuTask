package com.nikola.task.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nikola.task.R;
import com.nikola.task.manager.shared_prefs.SharedPrefsManager;
import com.nikola.task.manager.volley_callback.VolleyServiceListener;
import com.nikola.task.manager.volley_callback.VolleyServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.nikola.task.utils.Constants.EMAIL_KEY;
import static com.nikola.task.utils.Constants.EMAIL_VALUE;
import static com.nikola.task.utils.Constants.PASSWORD_KEY;
import static com.nikola.task.utils.Constants.PASSWORD_VALUE;
import static com.nikola.task.utils.Constants.TOKEN_KEY;
import static com.nikola.task.utils.Constants.URL_BASE_LOGIN;

public class LoginActivity extends AppCompatActivity {

    /**
     * Shared preferences manager
     */
    private SharedPrefsManager prefsManager;

    /**
     * Token data
     */
    private String token;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_asset));

        prefsManager = new SharedPrefsManager(getApplicationContext());

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
                    prefsManager.setLoggedIn(true);
                } else {
                    prefsManager.setLoggedIn(false);
                    //Alert user that validation is not good
                    Toast.makeText(LoginActivity.this, R.string.validation_alert, Toast.LENGTH_SHORT).show();
                }

                if (prefsManager.isLoggedIn()) {
                    //Go to details screen after login is successful and destroy login activity
                    startActivity(new Intent(LoginActivity.this, DetailsActivity.class));
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

        String jsonString = "{" + EMAIL_KEY + ":" + EMAIL_VALUE + "," + PASSWORD_KEY + ":" + PASSWORD_VALUE + "}";

        VolleyServiceManager volleyServiceManager = new VolleyServiceManager(getApplicationContext(), new VolleyServiceListener() {

            @Override
            public void onResponseCallback(final JSONObject response) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            token = response.getString("access_token");
                            storeTokenData(token);
                            System.out.println("############# LOGIN RESPONSE: " + response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onErrorCallback(final VolleyError error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error.printStackTrace();
                        System.out.println("############# LOGIN ERROR: " + error.toString());
                    }
                });
            }
        });

        try {
            //Request volley data
            volleyServiceManager.requestVolleyData(URL_BASE_LOGIN, jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Store token data
     * <p>
     * Store token data from Json response to shared preferences
     *
     * @param data token data
     */
    private void storeTokenData(String data) {

        prefsManager.storeData(TOKEN_KEY, data);
    }
}
