package com.elegion.tracktor.data.model;

import java.util.Date;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Track extends RealmObject {

    @PrimaryKey
    private long mId;

    private Date mDate;

    private long mDuration;

    private Double mDistance;

    private String mImageBase64;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public Double getDistance() {
        return mDistance;
    }

    public void setDistance(Double distance) {
        mDistance = distance;
    }

    public String getImageBase64() {
        return mImageBase64;
    }

    public void setImageBase64(String imageBase64) {
        mImageBase64 = imageBase64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return mId == track.mId &&
                mDuration == track.mDuration &&
                Objects.equals(mDate, track.mDate) &&
                Objects.equals(mDistance, track.mDistance) &&
                Objects.equals(mImageBase64, track.mImageBase64) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mDate, mDuration, mDistance, mImageBase64);
    }
}
