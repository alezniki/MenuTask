package com.example.nikola.task.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.nikola.task.R;
import com.example.nikola.task.entities.ImageData;
import com.example.nikola.task.entities.RestaurantData;
import com.example.nikola.task.manager.shared_prefs.SharedPrefsManager;
import com.example.nikola.task.manager.volley_callback.VolleyServiceListener;
import com.example.nikola.task.manager.volley_callback.VolleyServiceManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.nikola.task.utils.Constants.ACCESS_TOKEN_KEY;
import static com.example.nikola.task.utils.Constants.MAJOR_KEY;
import static com.example.nikola.task.utils.Constants.MAJOR_VALUE;
import static com.example.nikola.task.utils.Constants.MINOR_KEY;
import static com.example.nikola.task.utils.Constants.MINOR_VAlUE;
import static com.example.nikola.task.utils.Constants.TABLE_BEACON;
import static com.example.nikola.task.utils.Constants.TOKEN_KEY;
import static com.example.nikola.task.utils.Constants.URL_BASE_DETAILS;

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
     * JSON Object
     */
    private JSONObject jsonObject;

    /**
     * Stored token data
     */
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        prefsManager = new SharedPrefsManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvOpen = (TextView) findViewById(R.id.tv_open);
        ivThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);

        //Retrieve token data
        token = prefsManager.getStoredData(TOKEN_KEY, "");
        System.out.println("############# STORED TOKEN DATA " + token);

        //Request response data
        requestData();
    }


    /**
     * Request JSON data
     * <p>
     * Request data via Volley POST request
     */
    private void requestData() {

        String jsonString = "{" + TABLE_BEACON + ":{" + MAJOR_KEY + ":" + MAJOR_VALUE + "," + MINOR_KEY + ":" + MINOR_VAlUE + "}," + ACCESS_TOKEN_KEY + ":" + token + "}";

        VolleyServiceManager volleyServiceManager = new VolleyServiceManager(getApplicationContext(), new VolleyServiceListener() {

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

                            // Refresh screen UI
                            refreshUI(name, intro, message, isOpen, thumbnail);
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
                        System.out.println("############# DETAILS ERROR: " + error.toString());
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
     * Refresh screen view
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

        Typeface typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_asset));

        tvName.setText(restaurantData.getName());
        tvName.setTypeface(typeface);

        tvMessage.setText(restaurantData.getMessage());
        tvMessage.setTypeface(typeface);

        tvIntro.setText(restaurantData.getIntro());
        tvIntro.setTypeface(typeface);

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
    public void logout() {
        prefsManager.clearData(token);
        startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
        finish();
    }
}