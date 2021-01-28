package ru.geekbrains.weatherapplication.service;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.geekbrains.weatherapplication.R;

import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;


public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();
    private int messageId = 0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (DEBUG) {
            Log.d(TAG, remoteMessage.getNotification().getBody());
        }

        String title = remoteMessage.getNotification().getTitle();
        if (title == null){
            title = getString(R.string.app_name);
        }
        String text = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.mipmap.ic_weather_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon( getBitmapfromUrl(remoteMessage.getNotification().getImageUrl().toString()));
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "onNewToken -> token=" + token);
        sendRegistrationToServer(token);
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    private void sendRegistrationToServer(String token) {
    }

}
