package com.samp.mobile.game.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.samp.mobile.R;

import java.util.Formatter;

public class LoadingScreen {

    private Activity activity;
    private TextView percentText;
    private ConstraintLayout mainLayout;
    //ProgressBar progressbar2;

    public LoadingScreen(Activity activity) {
        this.activity = activity;

        // Inflate layout
        mainLayout = (ConstraintLayout) activity.getLayoutInflater().inflate(R.layout.loadingscreen, null);
        activity.addContentView(mainLayout, new ConstraintLayout.LayoutParams(-1, -1));

        // Initialize percent text (make sure your loadingscreen.xml has this ID)
        percentText = mainLayout.findViewById(R.id.percentText);
        mainLayout.setVisibility(View.INVISIBLE);
    }

    // Show the loading screen
    public void show() {
        mainLayout.setVisibility(View.VISIBLE);
    }

    // Hide the loading screen
    public void hide() {
        mainLayout.setVisibility(View.INVISIBLE);
    }

    // Update progress percentage
    public void Update(int percent) {
        if (percentText != null && percent <= 100) {
            percentText.setText(new Formatter().format("%d%s", percent, "%").toString());
            //progressbar2.setProgress(percent);
        }
    }
}
