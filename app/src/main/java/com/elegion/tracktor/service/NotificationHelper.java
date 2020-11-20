package com.elegion.tracktor.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.elegion.tracktor.R;
import com.elegion.tracktor.ui.map.MainActivity;
import com.elegion.tracktor.util.DistanceConverter;
import com.elegion.tracktor.util.StringUtil;

public class NotificationHelper {

    public static final String CHANNEL_ID = "counter_service";
    public static final String CHANNEL_NAME = "Counter Service";
    public static final int NOTIFICATION_ID = 101;
    public static final int REQUEST_CODE_LAUNCH = 0;

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private final Context mContext;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public Notification showNotification() {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        return buildNotification();

    }

    public void updateNotification(long time, double distance) {
        boolean isInMiles = DistanceConverter.isDistanceInMiles(mContext);
        Notification notification = buildNotification(StringUtil.getTimeText(time), StringUtil.getDistanceText(distance, isInMiles));
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private Notification buildNotification() {
        return buildNotification("", "");
    }

    private Notification buildNotification(String time, String distance) {
        if (mNotificationBuilder == null) {
            configureNotificationBuilder();
        }
        String message = mContext.getString(R.string.notify_info, time, distance);
        return mNotificationBuilder.setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();
    }

    private void configureNotificationBuilder() {
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, REQUEST_CODE_LAUNCH,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mNotificationBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_my_location_white_24dp)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(mContext.getString(R.string.route_active))
                .setVibrate(new long[]{0})
                .setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (mNotificationManager != null && mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotificationManager.createNotificationChannel(channel);
        }
    }
}
