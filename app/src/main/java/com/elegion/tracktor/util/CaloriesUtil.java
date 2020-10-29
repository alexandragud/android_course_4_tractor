package com.elegion.tracktor.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.ActivityType;
import com.elegion.tracktor.data.model.Track;

public class CaloriesUtil {

    private static double getBMRForMale(double weight, int height, int age) {
        return 66.47 + 13.75 * weight + 5.003 * height - 6.755 * age;
    }

    private static double getBMRForFemale(double weight, int height, int age) {
        return 655.1 + 9.563 * weight + 1.85 * height - 4.676 * age;
    }

    public static double getBmr(double weight, int height, int age, boolean isMale) {
        return (isMale) ? getBMRForMale(weight, height, age) : getBMRForFemale(weight, height, age);
    }

    public static double getBmrFromPrefs(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int age = Integer.parseInt(preferences.getString(context.getString(R.string.pref_key_age), context.getString(R.string.pref_default_value_age)));
        double weight = Double.parseDouble(preferences.getString(context.getString(R.string.pref_key_weight), context.getString(R.string.pref_default_value_weight)));
        int height = Integer.parseInt(preferences.getString(context.getString(R.string.pref_key_height), context.getString(R.string.pref_default_value_height)));
        int gender = Integer.parseInt(preferences.getString(context.getString(R.string.pref_key_gender), context.getString(R.string.pref_default_value_gender)));
        boolean isMale = (gender == 1);
        return getBmr(weight, height, age, isMale);
    }

    public static long calculateCalories(double bmr, double speed, long timeInSec, ActivityType activityType) {
        if (speed > 0) {
            double timeInMinutes = timeInSec / 60.0;
            //используется время в минутах, чтобы на экране была видна разница в подсчетах
            return Math.round(timeInMinutes * bmr * activityType.getMet() / 24);
        }
        return 0;
    }

    public static long calculateCalories(double bmr, Track track) {
        return calculateCalories(bmr, track.getSpeed(), track.getDuration(), track.getActivityType());
    }
}
