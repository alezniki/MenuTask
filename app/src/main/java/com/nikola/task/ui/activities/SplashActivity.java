package com.nikola.task.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nikola.task.manager.shared_prefs.SharedPrefsManager;
import com.nikola.task.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    /**
     * Shared preferences manager
     */
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefsManager = new SharedPrefsManager(getApplicationContext());
        System.out.println("############# SPLASH SCREEN LOGGED IN : " + prefsManager.isLoggedIn());

        //Set timer delay seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (prefsManager.isLoggedIn()) {
                    //Go to Details screen if logged in
                    startActivity(new Intent(SplashActivity.this, DetailsActivity.class));
                    finish();
                } else {
                    //Go to Login screen otherwise
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, Constants.SPLASH_TIMEOUT_DELAY);
    }
}
