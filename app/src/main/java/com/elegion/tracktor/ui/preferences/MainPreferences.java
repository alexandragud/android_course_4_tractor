package com.elegion.tracktor.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.util.CaloriesUtil;

import java.util.Arrays;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;

public class MainPreferences extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainPreferences.class.getSimpleName();

    @Inject
    RealmRepository mRepository;

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
        setSummaryFor(findPreference(getString(R.string.pref_key_gender)));
        setSummaryFor(findPreference(getString(R.string.pref_key_age)));
        setSummaryFor(findPreference(getString(R.string.pref_key_height)));
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Scope scope = Toothpick.openScopes(App.class, MainPreferences.class);
        Toothpick.inject(this, scope);
    }

    @Override
    public void onDetach() {
        Toothpick.closeScope(MainPreferences.class);
        super.onDetach();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummaryFor(findPreference(key));
        String[] personParamKeys = new String[]{getString(R.string.pref_key_weight),
                getString(R.string.pref_key_gender),
                getString(R.string.pref_key_age),
                getString(R.string.pref_key_height)};
        if (Arrays.asList(personParamKeys).contains(key)) {
            double bmr = CaloriesUtil.getBmrFromPrefs(getContext());
            mRepository.updateAllTracksCalories(bmr);
        }
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
