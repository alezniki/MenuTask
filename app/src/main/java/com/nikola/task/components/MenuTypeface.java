package com.nikola.task.components;

import android.app.Application;

import com.example.nikola.task.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Menu Typeface
 * <p>
 * Default application typeface
 *
 * @author Nikola Aleksic
 */
public class MenuTypeface extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Define application default font path
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_asset))
                .setFontAttrId(R.attr.fontPath).build());
    }
}