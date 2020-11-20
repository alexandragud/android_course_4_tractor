package com.elegion.tracktor.service;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.GetRouteEvent;
import com.elegion.tracktor.event.StopBtnClickedEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.elegion.tracktor.service.NotificationHelper.NOTIFICATION_ID;

public class CounterService extends Service {

    public static final int UPDATE_INTERVAL = 60_000;
    public static final int UPDATE_FASTEST_INTERVAL = 5_000;
    public static final int UPDATE_MIN_DISTANCE = 20;

    private Disposable mTimerDisposable;
    private Long mShutdownDuration;

    private NotificationHelper mNotificationHelper;
    private TrackHelper mTrackHelper;
    private FusedLocationProviderClient mLocationProviderClient;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                mTrackHelper.addLocationToRoute(locationResult.getLastLocation());
                EventBus.getDefault().post(mTrackHelper.getNewPositionEvent());
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mTrackHelper = new TrackHelper();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mNotificationHelper = new NotificationHelper(this);
            Notification notification = mNotificationHelper.showNotification();
            startForeground(NOTIFICATION_ID, notification);

            final LocationRequest locationRequest = new LocationRequest()
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(UPDATE_FASTEST_INTERVAL)
                    .setSmallestDisplacement(UPDATE_MIN_DISTANCE)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            mLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
            startTimer();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            mShutdownDuration = Long.valueOf(preferences.getString(getString(R.string.pref_key_shutdown), "-1"));
        } else {
            Toast.makeText(this, R.string.permissions_denied, Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        mTimerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(CounterService.this::onTimerUpdate);
    }

    private void onTimerUpdate(long totalSeconds) {
        EventBus.getDefault().post(new UpdateTimerEvent(totalSeconds, mTrackHelper.getDistance()));
        mNotificationHelper.updateNotification(totalSeconds, mTrackHelper.getDistance());
        if (mShutdownDuration != -1 && totalSeconds == mShutdownDuration) {
            EventBus.getDefault().post(new StopBtnClickedEvent());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().post(mTrackHelper.getStopTrackEvent());
        mLocationProviderClient.removeLocationUpdates(mLocationCallback);
        mTimerDisposable.dispose();
        stopForeground(true);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetRoute(GetRouteEvent event) {
        EventBus.getDefault().post(mTrackHelper.getUpdateRouteEvent());
    }
}


