package com.nikola.task.components;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Menu Progress Bar View
 *
 * @author Nikola Aleksic
 */
public class MenuProgressBar {

    /**
     * Context context
     */
    private final Context context;

    /**
     * Progress bar view
     */
    private ProgressBar progressBar;

    /**
     * Constructor
     *
     * @param context context
     */
    public MenuProgressBar(Context context) {
        this.context = context;

        setup();
    }

    /**
     * Setup method
     */
    private void setup() {

        //Set view container holder
        ViewGroup holder = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();

        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        //Set layout params
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(Gravity.CENTER);

        //Add progress bar to holder view
        relativeLayout.addView(progressBar, 150, 150);
        holder.addView(relativeLayout, params);

        //Disable by default
        disableProgress();
    }

    /**
     * Enable progress bar
     */
    public void enableProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Disable progress bar
     */
    public void disableProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
