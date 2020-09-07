package com.elegion.tracktor.ui.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.elegion.tracktor.R;

public class MainPreferences extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainPreferences.class.getSimpleName();

    public static MainPreferences newInstance() {
        return new MainPreferences();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.tr_pref, rootKey);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureSummaryEntries();
    }

    private void configureSummaryEntries() {
        setSummaryFor(findPreference(getString(R.string.pref_key_unit)));
        setSummaryFor(findPreference(getString(R.string.pref_key_shutdown)));
        setSummaryFor(findPreference(getString(R.string.pref_key_weight)));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummaryFor(findPreference(key));
    }

    private void setSummaryFor(Preference preference) {
        if (preference instanceof ListPreference)
            preference.setSummary(((ListPreference) preference).getEntry());
        else if (preference instanceof EditTextPreference)
            preference.setSummary(((EditTextPreference) preference).getText());
        else
            Log.d(TAG, "check preferences type");
    }
}
