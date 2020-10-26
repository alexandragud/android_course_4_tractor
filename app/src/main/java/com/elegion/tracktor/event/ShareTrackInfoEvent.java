package com.elegion.tracktor.event;

import com.elegion.tracktor.data.model.Track;

public class ShareTrackInfoEvent {

    private Track mTrack;

    public ShareTrackInfoEvent(Track track) {
        mTrack = track;
    }

    public Track getTrack() {
        return mTrack;
    }
}
