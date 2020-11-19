package com.elegion.tracktor.ui.results;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.SortType;
import com.elegion.tracktor.data.model.ActivityType;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.util.CaloriesUtil;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import java.util.List;

import javax.inject.Inject;

public class ResultsViewModel extends ViewModel {

    @Inject
    RealmRepository mRepository;

    private double personBMR;

    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();

    private MutableLiveData<String> mSelectedTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mSelectedDistanceText = new MutableLiveData<>();
    private MutableLiveData<Bitmap> mSelectedImage = new MutableLiveData<>();
    private MutableLiveData<String> mSelectedSpeed = new MutableLiveData<>();
    private MutableLiveData<String> mSelectedCalories = new MutableLiveData<>();
    private MutableLiveData<ActivityType> mSelectedActivity = new MutableLiveData<>();
    private MutableLiveData<String> mSelectedComment = new MutableLiveData<>();
    private MutableLiveData<String> mSelectedDate = new MutableLiveData<>();


    private long selectedTrackId;

    private boolean isAscendingSorted;
    private String sortedField;
    private boolean isInMiles;

    public ResultsViewModel() {
        isAscendingSorted = true;
        sortedField = SortType.BY_DATE.getFieldName();
        isInMiles = false;
    }

    public void loadTracks() {
        mTracks.postValue(mRepository.getAllWithSort(sortedField, isAscendingSorted));
    }

    public void selectTrack(long trackId) {
        selectedTrackId = trackId;
    }

    public void setPersonBMR(double value) {
        personBMR = value;
    }

    public void loadSelectedTrack() {
        Track track = mRepository.getItem(selectedTrackId);
        mSelectedTimeText.postValue(StringUtil.getTimeText(track.getDuration()));
        mSelectedDistanceText.postValue(StringUtil.getDistanceText(track.getDistance(), isInMiles));
        mSelectedImage.postValue(ScreenshotMaker.fromBase64(track.getImageBase64()));
        mSelectedSpeed.postValue(StringUtil.getSpeedText(track.getSpeed(), isInMiles));
        mSelectedDate.postValue(StringUtil.getDateText(track.getDate()));
        mSelectedActivity.postValue(track.getActivityType());
        mSelectedComment.postValue(track.getComment());
        mSelectedCalories.postValue(StringUtil.getCaloriesText(track.getCalories()));
    }


    public boolean deleteSelectedTrack() {
        return deleteTrack(selectedTrackId);
    }

    public boolean deleteTrack(long id) {
        boolean result = mRepository.deleteItem(id);
        loadTracks();
        return result;
    }

    public void updateTrackActivity(int activityId) {
        Track track = mRepository.getItem(selectedTrackId);
        ActivityType type = mRepository.getActivityType(activityId);
        track.setActivityType(type);
        updateTrack(track);
    }

    public Track getSelectedTrack() {
        return getTrack(selectedTrackId);
    }

    public Track getTrack(long id) {
        return mRepository.getItem(id);
    }


    public void updateCaloriesForSelectedTrack() {
        updateCalories(selectedTrackId);
        loadSelectedTrack();
    }

    public void updateCalories(long trackId) {
        Track track = mRepository.getItem(trackId);
        long calories = CaloriesUtil.calculateCalories(personBMR, track);
        track.setCalories(calories);
        updateTrack(track);
    }

    public void updateTrack(Track track) {
        mRepository.updateItem(track);
    }

    public void changeSortDirection(boolean isAscending) {
        isAscendingSorted = isAscending;
        loadTracks();
    }

    public void isDistanceInMiles(boolean value) {
        isInMiles = value;
    }

    public void changeSortType(SortType type) {
        sortedField = type.getFieldName();
        loadTracks();
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }

    public MutableLiveData<String> getSelectedTimeText() {
        return mSelectedTimeText;
    }

    public MutableLiveData<String> getSelectedDistanceText() {
        return mSelectedDistanceText;
    }

    public MutableLiveData<Bitmap> getSelectedImage() {
        return mSelectedImage;
    }

    public MutableLiveData<String> getSelectedSpeed() {
        return mSelectedSpeed;
    }

    public MutableLiveData<String> getSelectedCalories() {
        return mSelectedCalories;
    }

    public MutableLiveData<ActivityType> getSelectedActivity() {
        return mSelectedActivity;
    }

    public MutableLiveData<String> getSelectedComment() {
        return mSelectedComment;
    }

    public MutableLiveData<String> getSelectedDate() {
        return mSelectedDate;
    }

    public List<String> getAllActivities() {
        return mRepository.getActivitiesList();
    }
}
