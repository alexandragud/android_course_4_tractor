package com.elegion.tracktor.event;

public class DeleteTrackEvent {

    private long trackId;

    public DeleteTrackEvent(long trackId) {
        this.trackId = trackId;
    }

    public long getTrackId() {
        return trackId;
    }
}
