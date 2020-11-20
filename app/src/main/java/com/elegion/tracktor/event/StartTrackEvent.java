package com.elegion.tracktor.event;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class StartTrackEvent extends NewPositionEvent {

    private LatLng mStartPosition;

    public StartTrackEvent(@NonNull LatLng startPosition) {
        mStartPosition = startPosition;
    }

    public LatLng getStartPosition() {
        return mStartPosition;
    }
}
