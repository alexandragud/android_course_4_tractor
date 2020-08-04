package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;
import com.elegion.tracktor.util.ScreenshotMaker;

public class ResultsActivity extends SingleFragmentActivity {
    public static final String DISTANCE_KEY = "DISTANCE_KEY";
    public static final String TIME_KEY = "TIME_KEY";
    public static final String SCREENSHOT_KEY = "SCREENSHOT_KEY";

    public static void start(@NonNull Context context, String distance, String time, Bitmap screenshot) {
        String filePath = ScreenshotMaker.saveToInternalStorage(screenshot, context.getApplicationContext());
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(DISTANCE_KEY, distance);
        intent.putExtra(TIME_KEY, time);
        //intent.putExtra(SCREENSHOT_KEY, ScreenshotMaker.toBase64(screenshot));
        intent.putExtra(SCREENSHOT_KEY, filePath);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return ResultsDetailsFragment.newInstance(getIntent().getExtras());
    }
}
