package com.nikola.task.manager.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.nikola.task.utils.Constants;

import static com.nikola.task.utils.Constants.LOGIN_KEY;
import static com.nikola.task.utils.Constants.TOKEN_KEY;

/**
 * Shared Preferences Manager
 *
 * @author Nikola Aleksic
 */
public class SharedPrefsManager {

    /**
     * Context
     */
    private final Context context;

    /**
     * Shared preferences
     */
    private SharedPreferences sharedPreferences;

    /**
     * Shared preferences editor
     */
    private SharedPreferences.Editor editor;

    /**
     * Constructor
     *
     * @param context context
     */
    public SharedPrefsManager(Context context) {
        this.context = context;

        setup();
    }

    /**
     * Setup method
     */
    private void setup() {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    /**
     * Is logged in
     *
     * @return logged in
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN_KEY, false);
    }

    /**
     * Set logged in state
     *
     * @param isLogged is logged
     */
    public void setLoggedIn(boolean isLogged) {
        editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_KEY, isLogged).apply();
    }

    /**
     * Clear token data
     *
     * @param data token data
     */
    public void clearToken(String data) {
        editor = sharedPreferences.edit();
        editor.remove(data).apply();

        //Is data cleared
        boolean isClear = editor.commit();

        if (isClear) {
            setLoggedIn(false);
        }
    }

    /**
     * Store token data
     *
     * @param value token value
     */
    public void storeToken(String value) {
        editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, value).apply();
    }

    /**
     * Get stored token data
     */
    public String getTokenData() {
        return sharedPreferences.getString(TOKEN_KEY, "");
    }
}
