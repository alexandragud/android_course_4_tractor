package com.elegion.tracktor.event;

public class GetTrackResultEvent {

    private int trackId;

    public GetTrackResultEvent(int trackId) {
        this.trackId = trackId;
    }

    public int getTrackId() {
        return trackId;
    }
}
