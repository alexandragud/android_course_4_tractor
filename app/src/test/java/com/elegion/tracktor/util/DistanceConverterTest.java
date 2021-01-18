package com.elegion.tracktor.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;

import com.elegion.tracktor.TestApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, application = TestApp.class)
public class DistanceConverterTest {

    @Test
    public void isDistanceInMiles_isCorrect() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString("unit", "2").commit();
        assertTrue(DistanceConverter.isDistanceInMiles(context));
    }


    @Test
    public void isDistanceInMiles_NullException() {
        assertThrows(NullPointerException.class, () -> DistanceConverter.isDistanceInMiles(null));
    }

    @Test
    public void getInMetersOrKM_moreThanKm_isCorrect() {
        assertEquals("1.00 км.", DistanceConverter.getInMetersOrKM(1001));
        assertEquals("1.06 км.", DistanceConverter.getInMetersOrKM(1059));
    }

    @Test
    public void getInMetersOrKM_lessThanKm_isCorrect() {
        assertEquals("999.00 м.", DistanceConverter.getInMetersOrKM(999));
        assertEquals("999.10 м.", DistanceConverter.getInMetersOrKM(999.1));
        assertEquals("999.19 м.", DistanceConverter.getInMetersOrKM(999.189));
    }

    @Test
    public void getInMetersOrKM_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> DistanceConverter.getInMetersOrKM(-1));
    }

    @Test
    public void getInMeters_isCorrect() {
        assertEquals("100.00 м.", DistanceConverter.getInMeters(100));
        assertEquals("100.10 м.", DistanceConverter.getInMeters(100.1));
        assertEquals("100.11 м.", DistanceConverter.getInMeters(100.109));
    }

    @Test
    public void getInKM_isCorrect() {
        assertEquals("1.19 км.", DistanceConverter.getInKM(1190));
        assertEquals("1.20 км.", DistanceConverter.getInKM(1199));
    }

    @Test
    public void getInFeetOrMiles_isCorrect() {
        assertEquals("1.864 миль (9843 футов)", DistanceConverter.getInFeetOrMiles(3000));
    }

    @Test
    public void getInFeet_isCorrect() {
        assertEquals("984 футов", DistanceConverter.getInFeet(300));
    }

    @Test
    public void getInMiles_isCorrect() {
        assertEquals("1.864 миль", DistanceConverter.getInMiles(3000));
    }

    @Test
    public void getSpeedInMilesPerHour_isCorrect() {
        assertEquals("223.7 миль/час", DistanceConverter.getSpeedInMilesPerHour(100));
    }
}