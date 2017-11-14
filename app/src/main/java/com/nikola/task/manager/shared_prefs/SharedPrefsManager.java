package com.nikola.task.manager.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.nikola.task.utils.Constants;

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
        return sharedPreferences.getBoolean(Constants.LOGIN_KEY, false);
    }

    /**
     * Set logged in state
     *
     * @param isLogged is logged
     */
    public void setLoggedIn(boolean isLogged) {
        editor = sharedPreferences.edit();
        editor.putBoolean(Constants.LOGIN_KEY, isLogged).apply();
    }

    /**
     * Clear prefs data
     *
     * @param data prefs data
     */
    public void clearData(String data) {
        editor = sharedPreferences.edit();
        editor.remove(data).apply();

        //Is prefs data clear
        boolean isClear = editor.commit();

        if (isClear) {
            setLoggedIn(false);
        }
    }

    /**
     * Store prefs data
     *
     * @param value data value
     */
    public void storeData(String value) {
        editor = sharedPreferences.edit();
        editor.putString(Constants.TOKEN_KEY, value).apply();
    }

    /**
     * Get stored prefs data
     */
    public String getStoredData() {
        return sharedPreferences.getString(Constants.TOKEN_KEY, "");
    }
}
