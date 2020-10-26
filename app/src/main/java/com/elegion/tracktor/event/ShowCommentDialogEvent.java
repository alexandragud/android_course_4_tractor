package com.elegion.tracktor.event;

import com.elegion.tracktor.data.model.Track;

public class ShowCommentDialogEvent {

    private Track mTrack;

    public ShowCommentDialogEvent(Track track) {
        mTrack = track;
    }

    public Track getTrack() {
        return mTrack;
    }
}
