package com.elegion.tracktor.ui.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> startEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> stopEnabled = new MutableLiveData<>();
    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mDistanceText = new MutableLiveData<>();

    @Inject
    RealmRepository mRepository;
    private long mDurationRaw;
    private double mDistanceRaw;

    public MainViewModel(){
        EventBus.getDefault().register(this);
        startEnabled.setValue(true);
        stopEnabled.setValue(false);
    }

    public void switchButtons() {
        startEnabled.setValue(!startEnabled.getValue());
        stopEnabled.setValue(!stopEnabled.getValue());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTimer(UpdateTimerEvent event){
        mTimeText.postValue(StringUtil.getTimeText(event.getSeconds()));
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
        mDurationRaw = event.getSeconds();
        mDistanceRaw = event.getDistance();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event){
        mDistanceRaw = event.getDistance();
        mDistanceText.postValue(StringUtil.getDistanceText(mDistanceRaw));
        startEnabled.postValue(false);
        stopEnabled.postValue(true);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event){
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
    }

    public MutableLiveData<Boolean> getStartEnabled() {
        return startEnabled;
    }

    public MutableLiveData<Boolean> getStopEnabled() {
        return stopEnabled;
    }

    public MutableLiveData<String> getTimeText() {
        return mTimeText;
    }

    public MutableLiveData<String> getDistanceText() {
        return mDistanceText;
    }

    @Override
    protected void onCleared() {
        EventBus.getDefault().unregister(this);
        super.onCleared();
    }

    public void clear(){
        mTimeText.setValue("");
        mDistanceText.setValue("");
    }

    public long saveResults(String base64image) {
        Track track = createTrackFromData(mDurationRaw, mDistanceRaw, base64image);
        return mRepository.insertItem(track);
    }

    private Track createTrackFromData(long duration, double distance, String base64image) {
        Track track = new Track();
        track.setDistance(distance);
        track.setDuration(duration);
        track.setImageBase64(base64image);
        track.setDate(new Date());
        return track;
    }


}
