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

    private Double mSpeed;

    private ActivityType mActivityType;

    private String mComment;

    private long mCalories;

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

    public void setSpeed(Double speed) {
        mSpeed = speed;
    }

    public Double getSpeed(){
        return mSpeed;
    }

    public ActivityType getActivityType() {
        return mActivityType;
    }

    public void setActivityType(ActivityType activityType) {
        mActivityType = activityType;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public long getCalories() {
        return mCalories;
    }

    public void setCalories(long calories) {
        mCalories = calories;
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

    @Override
    public String toString() {
        return "Track{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mDuration=" + mDuration +
                ", mDistance=" + mDistance +
                ", mSpeed=" + mSpeed +
                ", mActivityValue='" + mActivityType.getName() + '\'' +
                '}';
    }
}
