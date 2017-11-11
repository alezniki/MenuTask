package com.nikola.task.manager.volley_callback;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Volley Service Listener
 *
 * @author Nikola Aleksic
 */
public interface VolleyServiceListener {

    /**
     * On Response Callback
     *
     * @param response response
     */
    void onResponseCallback(JSONObject response);

    /**
     * On Error Callback
     *
     * @param error error
     */
    void onErrorCallback(VolleyError error);
}
