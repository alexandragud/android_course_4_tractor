package com.elegion.tracktor.event;

import com.elegion.tracktor.data.model.Track;

public class UpdateTrackEvent {

    private Track mTrack;

    public UpdateTrackEvent(Track track) {
        mTrack = track;
    }

    public Track getTrack() {
        return mTrack;
    }
}
