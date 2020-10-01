package com.elegion.tracktor.ui.map;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.StartBtnClickedEvent;
import com.elegion.tracktor.event.StopBtnClickedEvent;
import com.elegion.tracktor.service.CounterService;
import com.elegion.tracktor.ui.preferences.PreferenceActivity;
import com.elegion.tracktor.ui.results.ResultsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_STORAGE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_DENIED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
            showRequestRationaleDialog();
        } else {
            configureMap();
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
                PreferenceActivity.start(this);
                return true;
            case R.id.actionStatistic:
                ResultsActivity.start(this, ResultsActivity.LIST_ID);
                return true;
            case R.id.actionAbout:
                launchAboutScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchAboutScreen() {
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this,R.color.colorPrimary))
                .setShowTitle(true)
                .enableUrlBarHiding()
                .build();
        intent.launchUrl(this, Uri.parse("https://www.e-legion.com"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "from notify", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartBtnClicked(StartBtnClickedEvent event) {
        Intent serviceIntent = getServiceIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopBtnClicked(StopBtnClickedEvent event) {
        stopService(getServiceIntent());
    }

    @NonNull
    private Intent getServiceIntent() {
        return new Intent(this, CounterService.class);
    }

    private void showRequestRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.permissions_request_title)
                .setMessage(R.string.permissions_request_message)
                .setPositiveButton(R.string.ok, ((dialog, which) ->
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_LOCATION_STORAGE)))
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_STORAGE) {
            if (permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_DENIED) {
                Toast.makeText(this, R.string.back_off, Toast.LENGTH_LONG).show();
                finish();
            } else {
                configureMap();
            }
        }
    }

    private void configureMap() {
        TrackMapFragment map = getTrackMapFragment();
        map.configure();
    }

    private TrackMapFragment getTrackMapFragment() {
         return (TrackMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }


}