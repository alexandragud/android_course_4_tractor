package com.elegion.tracktor.ui.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> startEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> stopEnabled = new MutableLiveData<>();
    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mDistanceText = new MutableLiveData<>();

    private RealmRepository mRealmRepository;
    private long duration;
    private double distance;

    public MainViewModel(){
        EventBus.getDefault().register(this);
        startEnabled.setValue(true);
        stopEnabled.setValue(false);
        mRealmRepository = new RealmRepository();
    }

    public void switchButtons() {
        startEnabled.setValue(!startEnabled.getValue());
        stopEnabled.setValue(!stopEnabled.getValue());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTimer(UpdateTimerEvent event){
        mTimeText.postValue(StringUtil.getTimeText(event.getSeconds()));
        duration = event.getSeconds();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event){
        distance = event.getDistance();
        mDistanceText.postValue(StringUtil.getDistanceText(distance));
        startEnabled.postValue(false);
        stopEnabled.postValue(true);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event){
        distance = event.getDistance();
        mDistanceText.postValue(StringUtil.getDistanceText(distance));
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
        return mRealmRepository.createAndInsertTrackFrom(duration, distance, base64image);
    }
}
