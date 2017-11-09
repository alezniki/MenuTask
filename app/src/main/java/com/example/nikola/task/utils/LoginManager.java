package com.example.nikola.task.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.nikola.task.utils.Constants.LOGIN_STATE;
import static com.example.nikola.task.utils.Constants.LOGIN_STATE_KEY;

/**
 * Login Manager
 *
 * @author Nikola Aleksic
 */
public class LoginManager {

    /**
     * Context
     */
    private Context context;

    /**
     * Shared preferences
     */
    private SharedPreferences prefs;

    /**
     * Constructor
     *
     * @param context context
     */
    public LoginManager(Context context) {
        this.context = context;
        setup();
    }

    /**
     * Setup method
     */
    private void setup() {
        prefs = context.getSharedPreferences(LOGIN_STATE, 0);
    }

    /**
     * Is logged in
     *
     * @return logged in
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(LOGIN_STATE_KEY, false);
    }

    /**
     * Set logged in
     *
     * @param isLogged is logged state
     */
    public void setLoggedIn(boolean isLogged) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(LOGIN_STATE_KEY, isLogged);
        editor.apply();
    }
}
