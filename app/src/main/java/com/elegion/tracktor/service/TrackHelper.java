package com.elegion.tracktor.service;

import android.location.Location;

import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.NewPositionEvent;
import com.elegion.tracktor.event.StartTrackEvent;
import com.elegion.tracktor.event.StopTrackEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class TrackHelper {

    private final List<LatLng> mRoute = new ArrayList<>();
    private Location mLastLocation;
    private LatLng mLastPosition;
    private double mDistance = 0;
    private NewPositionEvent event;

    public void addLocationToRoute(Location location) {
        LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
        if (isRouteEmpty()) {
            mRoute.add(newPosition);
            event = getStartTrackEvent(newPosition);
        } else {
            if (positionChanged(newPosition)) {
                mRoute.add(newPosition);
                LatLng prevPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mDistance += SphericalUtil.computeDistanceBetween(prevPosition, newPosition);
                event = getAddPositionToRouteEvent(prevPosition, newPosition, mDistance);
            }
        }
        mLastLocation = location;
        mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
    }

    private boolean isRouteEmpty() {
        return mRoute.size() == 0 && mLastLocation == null && mLastPosition == null;
    }

    private boolean positionChanged(LatLng position) {
        return mLastLocation.getLongitude() != position.longitude || mLastLocation.getLatitude() != position.latitude;
    }

    private StartTrackEvent getStartTrackEvent(LatLng startPosition) {
        return new StartTrackEvent(startPosition);
    }

    private AddPositionToRouteEvent getAddPositionToRouteEvent(LatLng prevPosition, LatLng newPosition, double distance) {
        return new AddPositionToRouteEvent(prevPosition, newPosition, distance);
    }

    public NewPositionEvent getNewPositionEvent() {
        return event;
    }

    public StopTrackEvent getStopTrackEvent() {
        return new StopTrackEvent(mRoute);
    }

    public UpdateRouteEvent getUpdateRouteEvent() {
        return new UpdateRouteEvent(mRoute, mDistance);
    }

    public double getDistance() {
        return mDistance;
    }
}
