package com.elegion.tracktor.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.elegion.tracktor.R;

public class DistanceConverter {

    private static final int MILE_SIZE = 1609;
    private static final int KILOMETER_SIZE = 1000;
    private static final double FOOT_SIZE = 0.3048;

    public static boolean isDistanceInMiles(Context context) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mPreferences.getString(context.getString(R.string.pref_key_unit), "1").equals("2");
    }

    public static String getInMetersOrKM(double valueInMeters) {
        if (valueInMeters > KILOMETER_SIZE)
            return getInKM(valueInMeters);
        else
            return getInMeters(valueInMeters);
    }

    public static String getInMeters(double valueInMeters) {
        return StringUtil.getDistanceText(valueInMeters, 2, "м.");
    }

    public static String getInKM(double valueInMeters) {
        double valueInKM = valueInMeters / KILOMETER_SIZE;
        return StringUtil.getDistanceText(valueInKM, 2, "км.");
    }


    public static String getInFeetOrMiles(double valueInMeters) {
        return getInMiles(valueInMeters) + " (" + getInFeet(valueInMeters) + ")";
    }

    public static String getInFeet(double valueInMeters) {
        double valueInFeet = valueInMeters / FOOT_SIZE;
        return StringUtil.getDistanceText(valueInFeet, 0, "футов");
    }

    public static String getInMiles(double valueInMeters) {
        double valueInMiles = valueInMeters / MILE_SIZE;
        return StringUtil.getDistanceText(valueInMiles, 3, "миль");
    }

    public static String getSpeedInMilesPerHour(double valueInMetersPerSec) {
        double valueInMilesPerHour = valueInMetersPerSec / MILE_SIZE * 3600;
        return StringUtil.round(valueInMilesPerHour, 1) + " миль/час";
    }
}
