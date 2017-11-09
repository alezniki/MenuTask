package com.example.nikola.task.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.example.nikola.task.R;
import com.example.nikola.task.entities.ImageData;
import com.example.nikola.task.entities.RestaurantData;
import com.example.nikola.task.utils.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.nikola.task.utils.Constants.ACCESS_TOKEN;
import static com.example.nikola.task.utils.Constants.MAJOR;
import static com.example.nikola.task.utils.Constants.MAJOR_VALUE;
import static com.example.nikola.task.utils.Constants.MINOR;
import static com.example.nikola.task.utils.Constants.MINOR_VAlUE;
import static com.example.nikola.task.utils.Constants.SHARED_PREFS;
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

    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        loginManager = new LoginManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvOpen = (TextView) findViewById(R.id.tv_open);
        ivThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);

        //Retrieve stored token data
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN_KEY, "");
        System.out.println("############# STORED TOKEN DATA " + token);

        //JSON-encoded string
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
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_BASE_DETAILS, new JSONObject(data),
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

                                        // Refresh screen UI
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
        //Handle action bar item clicks
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            //Logout action
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Logout
     * <p>
     * Clears stored preferences after user logout
     */
    public void logout() {
        SharedPreferences preferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(ACCESS_TOKEN).apply();

        boolean isCleared = editor.commit();

        if (isCleared) {
            //Go to login screen after logout is successful
            loginManager.setLoggedIn(false);
            startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
            finish();
        }
    }
}
