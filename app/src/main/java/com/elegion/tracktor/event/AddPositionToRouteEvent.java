package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

public class AddPositionToRouteEvent extends NewPositionEvent  {

    private LatLng mLastPosition;
    private LatLng mNewPosition;
    private double mDistance;

    public AddPositionToRouteEvent(LatLng lastPosition, LatLng newPosition, double distance) {
        mLastPosition = lastPosition;
        mNewPosition = newPosition;
        mDistance = distance;
    }

    public LatLng getLastPosition() {
        return mLastPosition;
    }

    public LatLng getNewPosition() {
        return mNewPosition;
    }

    public double getDistance() {
        return mDistance;
    }
}
