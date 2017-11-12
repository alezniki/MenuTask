package com.nikola.task.manager.volley_callback;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.nikola.task.utils.Constants.VOLLEY_REQUEST;

/**
 * Volley Service Manager
 *
 * @author Nikola Aleksic
 */
public class VolleyServiceManager {

    /**
     * Context
     */
    private Context context;

    /**
     * Volley Service listener
     */
    private VolleyServiceListener listener;

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
    }

    /**
     * Cancel Volley Request
     */
    public void cancelVolleyRequest(){
        Volley.newRequestQueue(context).cancelAll(VOLLEY_REQUEST);
    }


}
