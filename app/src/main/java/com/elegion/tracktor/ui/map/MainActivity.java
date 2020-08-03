package com.elegion.tracktor.ui.map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.SetStartPositionToRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteEvent;
import com.elegion.tracktor.ui.results.ResultsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    public static final int LOCATION_REQUEST_CODE = 99;
    public static final int UPDATE_INTERVAL = 5000;
    public static final int UPDATE_FASTEST_INTERVAL = 2000;
    public static final int UPDATE_MIN_DISTANCE = 10;
    public static final int DEFAULT_ZOOM = 15;


    @BindView(R.id.counterContainer)
    FrameLayout counterContainer;

    private GoogleMap mMap;
    private FusedLocationProviderClient mLocationProviderClient;
    private LocationRequest mLocationRequest = new LocationRequest();
    private Location mLastLocation;
    private boolean isRouteStarted;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null && mMap != null) {
                Location newLocation = locationResult.getLastLocation();
                LatLng newPosition = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                if (mLastLocation != null && isRouteStarted) {
                    EventBus.getDefault().post(new AddPositionToRouteEvent(newPosition));
                    LatLng lastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.addPolyline(new PolylineOptions().add(lastPosition, newPosition));
                } else if (!isRouteStarted) {
                    EventBus.getDefault().post(new SetStartPositionToRouteEvent(newPosition));
                }
                mLastLocation = locationResult.getLastLocation();
                LatLng position = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(UPDATE_MIN_DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (savedInstanceState == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.setRetainInstance(true);
            mapFragment.getMapAsync(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.counterContainer, new CounterFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings:
                //todo
                break;
            case R.id.actionStatistic:
                //todo
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRoadStart(StartRouteEvent event) {
        mMap.clear();
        LatLng startPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(startPosition).title(getString(R.string.begin)));
        isRouteStarted = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRoadStop(StopRouteEvent event) {
        isRouteStarted = false;
        List<LatLng> route = event.getRoute();
        mMap.addMarker(new MarkerOptions().position(route.get(route.size() - 1)).title(getString(R.string.end)));
        takeScreenshot(route, bitmap -> {
            ResultsActivity.start(this, event.getDistance(), event.getTime(), bitmap);
        });

    }

    private void takeScreenshot(List<LatLng> route, GoogleMap.SnapshotReadyCallback snapshotReadyCallback) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : route) {
            builder.include(point);
        }
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
        mMap.moveCamera(cu);
        mMap.snapshot(snapshotReadyCallback);
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Запрос разрешений на получение местоположения")
                    .setMessage("Нам необходимо знать Ваше местоположение, чтобы приложение работало")
                    .setPositiveButton("OK", ((dialog, which) ->
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE)))
                    .create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Вы не дали разрешения!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}