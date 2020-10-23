package com.elegion.tracktor.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.ActivityType;

public class CaloriesCalculator {

    private int age;
    private double weight;
    private int height;
    private long timeInSec;
    private boolean isMale;
    private ActivityType activityType;
    private double speed;

    private CaloriesCalculator() {
    }

    private double getBMRForMale() {
        return 66.47 + 13.75 * weight + 5.003 * height - 6.755 * age;
    }

    private double getBMRForFemale() {
        return 655.1 + 9.563 * weight + 1.85 * height - 4.676 * age;
    }

    public long calculate() {
        if (speed>0) {
            double bmr = (isMale) ? getBMRForMale() : getBMRForFemale();
            double timeInMinutes = timeInSec / 60.0;
            //используется время в минутах, чтобы на экране была видна разница в подсчетах
            return Math.round(timeInMinutes * bmr * activityType.getMet() / 24);
        }
        return 0;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setTime(long timeInSec) {
        this.timeInSec = timeInSec;
    }

    public void setActivityType(ActivityType activity) {
        this.activityType = activity;
    }

    private void setGender(int gender) {
        CaloriesCalculator.this.isMale = (gender == 1);
    }

    public static Builder builder() {
        return new CaloriesCalculator().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setDataFromPreferences(Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            age = Integer.parseInt(preferences.getString(context.getString(R.string.pref_key_age), context.getString(R.string.pref_default_value_age)));
            CaloriesCalculator.this.weight = Double.parseDouble(preferences.getString(context.getString(R.string.pref_key_weight), context.getString(R.string.pref_default_value_weight)));
            CaloriesCalculator.this.height = Integer.parseInt(preferences.getString(context.getString(R.string.pref_key_height), context.getString(R.string.pref_default_value_height)));
            int gender = Integer.parseInt(preferences.getString(context.getString(R.string.pref_key_gender), context.getString(R.string.pref_default_value_gender)));
            setGender(gender);
            return this;
        }

        public CaloriesCalculator build() {
            return CaloriesCalculator.this;
        }

    }
}
