package com.example.nikola.task;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get stored token data
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String token = preferences.getString(SAVED_ACCESS_TOKEN, "");

        System.out.println("################ STORED TOKEN DATA " + token);

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
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_BASE, new JSONObject(data),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(final JSONObject response) {

                            //Prevent server errors
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("############# DETAILS RESPONSE " + response);
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
                                    System.out.println("############# DETAILS ERROR " + error.toString());
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
}
