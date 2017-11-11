package com.example.nikola.task.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.nikola.task.manager.shared_prefs.SharedPrefsManager;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.nikola.task.utils.Constants.SPLASH_TIMEOUT_DELAY;

public class SplashActivity extends AppCompatActivity {

    /**
     * Shared preferences manager
     */
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefsManager = new SharedPrefsManager(getApplicationContext());

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
        }, SPLASH_TIMEOUT_DELAY);
    }
}
