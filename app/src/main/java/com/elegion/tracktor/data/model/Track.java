package com.elegion.tracktor.data.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Track extends RealmObject {

    @PrimaryKey
    private long mId;

    private Date mDate;

    private long mDuration;

    private Double mDistance;

    private String mImageBase64;

    private RealmList<Location> mLocations;

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

    public RealmList<Location> getLocations() {
        return mLocations;
    }

    public void setLocations(RealmList<Location> locations) {
        mLocations = locations;
    }
}
