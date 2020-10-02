package com.elegion.tracktor.ui.results;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import java.util.List;

import javax.inject.Inject;

public class ResultsViewModel extends ViewModel {

    @Inject
    RealmRepository mRepository;

    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();

    private MutableLiveData<String> mSelectedTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mSelectedDistanceText = new MutableLiveData<>();
    private MutableLiveData<Bitmap> mSelectedImage = new MutableLiveData<>();

    private long selectedTrackId;

    public ResultsViewModel() {
    }

    public void loadTracks() {
        if (mTracks.getValue() == null || mTracks.getValue().isEmpty())
            mTracks.postValue(mRepository.getAll());
    }

    public void selectTrack(long trackId){
        selectedTrackId = trackId;
    }

    public void loadSelectedTrack() {
        Track track = mRepository.getItem(selectedTrackId);
        mSelectedTimeText.postValue(StringUtil.getTimeText(track.getDuration()));
        mSelectedDistanceText.postValue(StringUtil.getDistanceText(track.getDistance()));
        mSelectedImage.postValue(ScreenshotMaker.fromBase64(track.getImageBase64()));
    }

    public boolean deleteSelectedTrack(){
        return mRepository.deleteItem(selectedTrackId);
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
}
