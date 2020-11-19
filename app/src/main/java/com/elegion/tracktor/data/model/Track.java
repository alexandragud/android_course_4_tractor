package com.elegion.tracktor.data.model;

import com.elegion.tracktor.util.StringUtil;

import java.util.Date;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
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

    @Ignore
    private boolean isExpand;

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

    public Double getSpeed() {
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

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return mId == track.mId &&
                mDuration == track.mDuration &&
                mCalories == track.mCalories &&
                isExpand == track.isExpand &&
                Objects.equals(mDate, track.mDate) &&
                Objects.equals(mDistance, track.mDistance) &&
                Objects.equals(mImageBase64, track.mImageBase64) &&
                Objects.equals(mSpeed, track.mSpeed) &&
                Objects.equals(mActivityType, track.mActivityType) &&
                Objects.equals(mComment, track.mComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mDate, mDuration, mDistance, mImageBase64, mSpeed, mActivityType, mComment, mCalories, isExpand);
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

    public String getTrackInfo(boolean isInMiles) {
        return String.format("Дата: %s\nВремя: %s\nРасстояние: %s\nСредняя скорость: %s\nДеятельность: %s\nКалории: %s\nКомментарий: %s",
                StringUtil.getDateText(getDate()),
                StringUtil.getTimeText(getDuration()),
                StringUtil.getDistanceText(getDistance(), isInMiles),
                StringUtil.getSpeedText(getSpeed(), isInMiles),
                getActivityType().getName(),
                StringUtil.getCaloriesText(getCalories()),
                StringUtil.getComment(getComment()));
    }
}
