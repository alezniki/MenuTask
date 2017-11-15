package com.nikola.task.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nikola.task.R;
import com.nikola.task.components.MenuProgressBar;
import com.nikola.task.manager.shared_prefs.SharedPrefsManager;
import com.nikola.task.manager.volley_callback.VolleyServiceListener;
import com.nikola.task.manager.volley_callback.VolleyServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.nikola.task.utils.Constants.EMAIL_KEY;
import static com.nikola.task.utils.Constants.PASSWORD_KEY;
import static com.nikola.task.utils.Constants.URL_BASE_LOGIN;

public class LoginActivity extends AppCompatActivity {

    /**
     * Email input view
     */
    private EditText etEmail;

    /**
     * Password input view
     */
    private EditText etPassword;

    /**
     * Login button
     */
    private Button btnLogin;

    /**
     * Shared preferences manager
     */
    private SharedPrefsManager prefsManager;

    /**
     * Volley Service Manager
     */
    private VolleyServiceManager volleyServiceManager;

    /**
     * Menu progress bar
     */
    private MenuProgressBar progressBar;

    /**
     * Token data
     */
    private String token;

    /**
     * Email data
     */
    private String email;

    /**
     * Password data
     */
    private String password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.et_login_email);
        etPassword = (EditText) findViewById(R.id.et_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        prefsManager = new SharedPrefsManager(getApplicationContext());
        progressBar = new MenuProgressBar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate credentials
                loginValidation();
            }
        });
    }

    /**
     * Request JSON data
     * <p>
     * Request data via Volley POST request
     */
    private void requestData() {

        //Enable progress bar
        progressBar.enableProgress();

        String jsonString = "{" + EMAIL_KEY + ":" + email + "," + PASSWORD_KEY + ":" + password + "}";

        volleyServiceManager = new VolleyServiceManager(getApplicationContext(), new VolleyServiceListener() {
            @Override
            public void onResponseCallback(final JSONObject response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("############# LOGIN RESPONSE: " + response);

                        try {

                            // Store token data
                            token = response.getString("access_token");
                            prefsManager.storeToken(token);
                            prefsManager.setLoggedIn(true);

                            //Disable progress bar
                            progressBar.disableProgress();

                            startActivity(new Intent(LoginActivity.this, DetailsActivity.class));
                            finish();
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
                        //Disable progress bar and alert user that validation is not good
                        progressBar.disableProgress();
                        Toast.makeText(LoginActivity.this, R.string.validation_alert, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStop() {
        super.onStop();

        //Cancel request
        if (volleyServiceManager != null) {
            volleyServiceManager.cancelVolleyRequest();
        }
    }

    /**
     * Login validation
     * <p>
     * Validate input fields and set login action
     */
    private void loginValidation() {

        //Store login credentials
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.input_alert, Toast.LENGTH_SHORT).show();
        } else {
            //Request response data
            requestData();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //Inject Calligraphy into activity context
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
