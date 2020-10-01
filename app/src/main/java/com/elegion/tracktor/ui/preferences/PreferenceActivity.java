package com.elegion.tracktor.ui.preferences;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;

public class PreferenceActivity extends SingleFragmentActivity {

    public static void start(AppCompatActivity activity){
        Intent intent = new Intent(activity, PreferenceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return MainPreferences.newInstance();
    }
}
