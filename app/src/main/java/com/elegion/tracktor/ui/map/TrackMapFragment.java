package com.elegion.tracktor.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.GetRouteEvent;
import com.elegion.tracktor.event.StartTrackEvent;
import com.elegion.tracktor.event.StopCountEvent;
import com.elegion.tracktor.event.StopTrackEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.service.NotificationHelper;
import com.elegion.tracktor.ui.results.ResultsActivity;
import com.elegion.tracktor.util.DistanceConverter;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class TrackMapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    public static final int DEFAULT_ZOOM = 15;

    private GoogleMap mMap;
    private SharedPreferences mPreferences;

    @Inject
    MainViewModel mMainViewModel;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Scope scope = Toothpick.openScopes(App.class, TrackMapFragment.class)
                .installModules(new ViewModelModule(this));
        Toothpick.inject(this, scope);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public void configure() {
        getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainViewModel.isDistanceInMiles(DistanceConverter.isDistanceInMiles(getContext()));
        EventBus.getDefault().register(this);
        if (mMap != null)
            EventBus.getDefault().post(new GetRouteEvent());
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    private void initMap() {
        Context context = getContext();
        if (context != null && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.setMyLocationEnabled(true);
            new Handler(Looper.getMainLooper())
                    .postDelayed(
                            () ->  EventBus.getDefault().post(new GetRouteEvent()),
                            1000);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this::initMap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event) {
        if (mMap!=null) {
            mMap.addPolyline(new PolylineOptions().add(event.getLastPosition(), event.getNewPosition()).color(getRouteColor()).width(getRouteWidth()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(event.getNewPosition(), DEFAULT_ZOOM));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event) {
        mMap.clear();

        List<LatLng> route = event.getRoute();
        mMap.addPolyline(new PolylineOptions().addAll(route).color(getRouteColor()).width(getRouteWidth()));
        addMarker(route.get(0), getString(R.string.start), getIconStartId());
        zoomRoute(route);
        if (NotificationHelper.ACTION_STOP.equals(getActivity().getIntent().getAction())) {
            new Handler(Looper.getMainLooper())
                    .postDelayed(
                            () -> EventBus.getDefault().postSticky(new StopCountEvent()),
                            2000);
        }
    }

    private void addMarker(LatLng position, String text, int iconResourceId) {
        Bitmap iconBitmap = ScreenshotMaker.fromVector(iconResourceId, getContext());
        mMap.addMarker(new MarkerOptions().position(position).title(text).icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)));
    }

    private int getIconStartId() {
        return mPreferences.getInt(getString(R.string.pref_key_icon_start), R.drawable.ic_icon_start_standart);
    }

    private int getIconStopId() {
        return mPreferences.getInt(getString(R.string.pref_key_icon_stop), R.drawable.ic_icon_stop_standart);
    }

    private int getRouteColor() {
        return mPreferences.getInt(getString(R.string.pref_key_line_color), R.color.colorBlue);
    }

    private float getRouteWidth() {
        return mPreferences.getInt(getString(R.string.pref_key_line_width), 10);
    }

    private void zoomRoute(List<LatLng> route) {
        if (route.size() == 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.get(0), DEFAULT_ZOOM));
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng point : route) {
                builder.include(point);
            }
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = 100;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(),width, height, padding);
            mMap.moveCamera(cu);
        }
    }

    private void takeMapScreenshot(List<LatLng> route, GoogleMap.SnapshotReadyCallback snapshotReadyCallback) {
        zoomRoute(route);
        mMap.snapshot(snapshotReadyCallback);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRoute(StartTrackEvent event) {
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(event.getStartPosition(), DEFAULT_ZOOM));
        addMarker(event.getStartPosition(), getString(R.string.start), getIconStartId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRoute(StopTrackEvent event) {
        List<LatLng> route = event.getRoute();
        if (route.isEmpty()) {
            Toast.makeText(getContext(), R.string.dont_stay, Toast.LENGTH_SHORT).show();
        } else {
            addMarker(route.get(route.size() - 1), getString(R.string.end), getIconStopId());

            takeMapScreenshot(route, bitmap -> {
                int compression = Integer.parseInt(mPreferences.getString(getString(R.string.pref_key_compression), "25"));
                String base64image = ScreenshotMaker.toBase64(bitmap, compression);
                long resultId = mMainViewModel.saveResults(base64image);
                ResultsActivity.start(getContext(), resultId);
            });
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public void onDetach() {
        Toothpick.closeScope(TrackMapFragment.class);
        super.onDetach();
    }
}
