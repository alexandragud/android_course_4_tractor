package com.elegion.tracktor.event;

public class GetTrackResultEvent {

    private long trackId;

    public GetTrackResultEvent(long trackId) {
        this.trackId = trackId;
    }

    public long getTrackId() {
        return trackId;
    }
}
