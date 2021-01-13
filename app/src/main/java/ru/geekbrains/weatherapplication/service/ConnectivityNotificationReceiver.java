package ru.geekbrains.weatherapplication.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import ru.geekbrains.weatherapplication.R;

import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;


public class ConnectivityNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = ConnectivityNotificationReceiver.class.getSimpleName();
    private int messageId=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) {
            Log.d(TAG, "title="+context.getString(R.string.title_notification)+" body="+context.getString(R.string.no_network_notification));
        }

        if (!isConnected(context)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                    .setSmallIcon(R.mipmap.ic_weather_launcher)
                    .setContentTitle(context.getString(R.string.title_notification))
                    .setContentText(context.getString(R.string.no_network_notification));
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(messageId++, builder.build());
        }
    }

    private boolean isConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
