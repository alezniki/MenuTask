package com.nikola.task.manager.volley_callback;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.nikola.task.utils.Constants.VOLLEY_REQUEST;
import static com.nikola.task.utils.Constants.VOLLEY_TIMEOUT_SOCKET;

/**
 * Volley Service Manager
 *
 * @author Nikola Aleksic
 */
public class VolleyServiceManager {

    /**
     * Context
     */
    private final Context context;

    /**
     * Volley Service listener
     */
    private final VolleyServiceListener listener;

    /**
     * Constructor
     *
     * @param context  context
     * @param listener listener
     */
    public VolleyServiceManager(Context context, VolleyServiceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Request Volley Data
     *
     * @param url  base url data
     * @param data json data
     * @throws JSONException json exception
     */
    public void requestVolleyData(String url, String data) throws JSONException {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        listener.onResponseCallback(response);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        listener.onErrorCallback(error);
                    }
                });

        //Request volley queue
        Volley.newRequestQueue(context).add(request).setTag(VOLLEY_REQUEST);

        //Set retry policy
        RetryPolicy policy = new DefaultRetryPolicy(VOLLEY_TIMEOUT_SOCKET, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    /**
     * Cancel Volley Request
     * <p>
     * Cancel all requests with the given tag
     */
    public void cancelVolleyRequest() {
        Volley.newRequestQueue(context).cancelAll(VOLLEY_REQUEST);
    }
}
