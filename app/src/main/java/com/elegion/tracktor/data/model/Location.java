package com.elegion.tracktor.data.model;

import io.realm.RealmObject;

public class Location extends RealmObject {

    private double mLatitude;
    private double mLongitude;

    public Location(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public Location() {
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
