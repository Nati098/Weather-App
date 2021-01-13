package ru.geekbrains.weatherapplication.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import ru.geekbrains.weatherapplication.R;

public class ServiceNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = ServiceNotificationReceiver.class.getSimpleName();
    private int messageId=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.mipmap.ic_weather_launcher)
                .setContentTitle(context.getString(R.string.title_notification))
                .setContentText(context.getString(R.string.low_battery_notification));
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }

}
