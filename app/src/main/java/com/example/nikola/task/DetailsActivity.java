package com.example.nikola.task;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nikola.task.entities.ImageData;
import com.example.nikola.task.entities.RestaurantData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.nikola.task.LoginActivity.SAVED_ACCESS_TOKEN;

public class DetailsActivity extends AppCompatActivity {

    public final static String TABLE_BEACON = "table_beacon";
    public final static String MAJOR = "major";
    public final static int MAJOR_VALUE = 5;
    public final static String MINOR = "minor";
    public final static int MINOR_VAlUE = 1;
    public final static String ACCESS_TOKEN = "access_token";
    public final static String URL_BASE = "https://usemenu.com/playground/public/api/v2/restaurant/info?app_version=2.8.1";

    private TextView tvName;
    private TextView tvIntro;
    private TextView tvMessage;
    private TextView tvOpen;
    private ImageView ivThumbnail;

    private JsonObjectRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvOpen = (TextView) findViewById(R.id.tv_open);
        ivThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);

        // Get stored token data
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String token = preferences.getString(SAVED_ACCESS_TOKEN, "");
        System.out.println("############# STORED TOKEN DATA: " + token);

        String jsonString = "{" + TABLE_BEACON + ":{" + MAJOR + ":" + MAJOR_VALUE + "," + MINOR + ":" + MINOR_VAlUE + "}," + ACCESS_TOKEN + ":" + token + "}";

        //Request response data
        requestData(jsonString);
    }

    /**
     * Request JSON data
     * <p>
     * Request data via Volley POST request
     *
     * @param data json data
     */
    private void requestData(String data) {
        try {
            request = new JsonObjectRequest(Request.Method.POST, URL_BASE, new JSONObject(data),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(final JSONObject response) {

                            //Prevent server errors
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("############# DETAILS SCREEN VOLLEY RESPONSE: " + response);

                                    try {
                                        JSONObject jsonObject = response.getJSONObject("restaurant");
                                        JSONObject images = jsonObject.getJSONObject("images");

                                        String name = jsonObject.getString("name");
                                        String intro = jsonObject.getString("intro");
                                        String message = jsonObject.getString("welcome_message");
                                        Boolean isOpen = jsonObject.getBoolean("is_open");
                                        String thumbnail = images.getString("thumbnail_medium");

                                        refreshUI(name, intro, message, isOpen, thumbnail);

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
                                    System.out.println("############# DETAILS SCREEN VOLLEY ERROR: " + error.toString());
                                }
                            });
                        }
                    });

            //Request volley queue
            Volley.newRequestQueue(this).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh UI
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

        if (restaurantData.isOpen()) {
            tvOpen.setText(R.string.restaurant_open);
            tvOpen.setTextColor(ContextCompat.getColor(DetailsActivity.this, R.color.colorGreen));
        } else {
            tvOpen.setText(R.string.restaurant_closed);
            tvOpen.setTextColor(ContextCompat.getColor(DetailsActivity.this, R.color.colorRed));
        }

        // Load thumbnail url data
        Picasso.with(DetailsActivity.this).load(imageData.getUrl()).fit().into(ivThumbnail);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Cancel all volley requests
        Volley.newRequestQueue(getApplicationContext()).cancelAll(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Todo Logout logic
            System.out.println("############### LOGIN BUTTON CLICKED");
        }

        return super.onOptionsItemSelected(item);
    }
}
