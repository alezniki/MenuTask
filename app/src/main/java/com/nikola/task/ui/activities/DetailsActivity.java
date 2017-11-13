package com.nikola.task.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.nikola.task.R;
import com.nikola.task.entities.ImageData;
import com.nikola.task.entities.RestaurantData;
import com.nikola.task.manager.shared_prefs.SharedPrefsManager;
import com.nikola.task.manager.volley_callback.VolleyServiceListener;
import com.nikola.task.manager.volley_callback.VolleyServiceManager;
import com.nikola.task.ui.view.MenuProgressBar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.nikola.task.utils.Constants.ACCESS_TOKEN_KEY;
import static com.nikola.task.utils.Constants.MAJOR_KEY;
import static com.nikola.task.utils.Constants.MAJOR_VALUE;
import static com.nikola.task.utils.Constants.MINOR_KEY;
import static com.nikola.task.utils.Constants.MINOR_VAlUE;
import static com.nikola.task.utils.Constants.TABLE_BEACON;
import static com.nikola.task.utils.Constants.TOKEN_KEY;
import static com.nikola.task.utils.Constants.URL_BASE_DETAILS;

public class DetailsActivity extends AppCompatActivity {

    /**
     * Restaurant name view
     */
    private TextView tvName;

    /**
     * Restaurant intro view
     */
    private TextView tvIntro;

    /**
     * Restaurant welcome message view
     */
    private TextView tvMessage;

    /**
     * Open/Close view
     */
    private TextView tvOpen;

    /**
     * Restaurant image view
     */
    private ImageView ivThumbnail;

    /**
     * Shared preferences manager
     */
    private SharedPrefsManager prefsManager;

    /**
     * Volley Service Manager
     */
    private VolleyServiceManager volleyServiceManager;

    /**
     * JSON Object
     */
    private JSONObject jsonObject;

    /**
     * Stored token data
     */
    private String token;

    /**
     * Typeface
     */
    private Typeface typeface;

    /**
     * Menu progress bar
     */
    private MenuProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        prefsManager = new SharedPrefsManager(getApplicationContext());

        typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_asset));

        tvName = (TextView) findViewById(R.id.tv_name);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvOpen = (TextView) findViewById(R.id.tv_open);
        ivThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        progressBar = new MenuProgressBar(this);

        //Retrieve token data
        token = prefsManager.getStoredData(TOKEN_KEY, "");
        System.out.println("############# STORED TOKEN DATA " + token);

        //Request response data
        requestData();

        //Enable progress bar
        progressBar.enableProgress();
    }

    /**
     * Request JSON data
     * <p>
     * Request data via Volley POST request
     */
    private void requestData() {
        String jsonString = "{" + TABLE_BEACON + ":{" + MAJOR_KEY + ":" + MAJOR_VALUE + "," + MINOR_KEY + ":" + MINOR_VAlUE + "}," + ACCESS_TOKEN_KEY + ":" + token + "}";

        volleyServiceManager = new VolleyServiceManager(getApplicationContext(), new VolleyServiceListener() {
            @Override
            public void onResponseCallback(final JSONObject response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("############# DETAILS RESPONSE: " + response);

                        try {
                            jsonObject = response.getJSONObject("restaurant");
                            JSONObject images = jsonObject.getJSONObject("images");

                            String name = jsonObject.getString("name");
                            String intro = jsonObject.getString("intro");
                            String message = jsonObject.getString("welcome_message");
                            Boolean isOpen = jsonObject.getBoolean("is_open");
                            String thumbnail = images.getString("thumbnail_medium");

                            //Refresh screen UI
                            refreshUI(name, intro, message, isOpen, thumbnail);

                            //Disable progress bar
                            progressBar.disableProgress();
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
                        System.out.println("############# DETAILS ERROR: " + error.toString());
                        error.printStackTrace();

                        //Disable progress bar
                        progressBar.disableProgress();
                    }
                });
            }
        });

        try {
            // Request Volley data
            volleyServiceManager.requestVolleyData(URL_BASE_DETAILS, jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh UI
     * <p>
     * Refresh UI screen view
     *
     * @param name      restaurant name
     * @param intro     restaurant intro
     * @param message   welcome message
     * @param isOpen    is open
     * @param thumbnail thumbnail url
     */
    private void refreshUI(String name, String intro, String message, boolean isOpen, String thumbnail) {
        RestaurantData restaurantData = new RestaurantData(name, intro, message, isOpen);
        ImageData imageData = new ImageData(thumbnail);

        tvName.setText(restaurantData.getName());
        tvName.setTypeface(typeface);

        tvMessage.setText(restaurantData.getMessage());
        tvMessage.setTypeface(typeface);

        tvIntro.setText(restaurantData.getIntro());
        tvIntro.setTypeface(typeface);
        tvIntro.setMovementMethod(new ScrollingMovementMethod());

        tvOpen.setTypeface(typeface);
        tvOpen.setTextColor(ContextCompat.getColor(DetailsActivity.this, R.color.colorAccent));

        if (restaurantData.isOpen()) {
            tvOpen.setText(R.string.restaurant_open);
        } else {
            tvOpen.setText(R.string.restaurant_closed);
        }

        //Load thumbnail url data
        Picasso.with(DetailsActivity.this).load(imageData.getUrl()).fit().into(ivThumbnail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add menu items to the action bar
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item clicks
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Logout
     * <p>
     * Clear stored preferences after user logout
     */
    private void logout() {
        prefsManager.clearData(token);
        startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        volleyServiceManager.cancelVolleyRequest();
    }
}