package ru.geekbrains.weatherapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import ru.geekbrains.weatherapplication.data.dto.CityListItem;
import ru.geekbrains.weatherapplication.data.request.MainRequest;
import ru.geekbrains.weatherapplication.data.request.WeatherRequest;

import static androidx.core.app.JobIntentService.enqueueWork;
import static ru.geekbrains.weatherapplication.service.WebApiService.WEATHER_REQUEST_MODE;
import static ru.geekbrains.weatherapplication.service.WebApiService.WEATHER_REQUEST_RESULT;


public class ApiDataReceiver extends Observable implements Runnable {
    private static final String TAG = ApiDataReceiver.class.getSimpleName();

    private Context context;
    private final Observer observer;

    private CityListItem city;
    private int requestMode;

    private MainRequest weatherRequest;

    public ApiDataReceiver(Context context, Observer observer, CityListItem city, int requestMode) {
        this.context = context;
        this.observer = observer;
        this.city = city;
        this.requestMode = requestMode;
    }

    @Override
    public void run() {
        Intent intentForService = new Intent(context, WebApiService.class);
        intentForService.putExtra(WEATHER_REQUEST_MODE, requestMode);
        intentForService.putExtra("lat", city.getCoord().getLat());
        intentForService.putExtra("lon", city.getCoord().getLon());

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                update(intent.getParcelableExtra(WEATHER_REQUEST_RESULT));
            }
        };
        IntentFilter filter = new IntentFilter(WEATHER_REQUEST_RESULT);
        context.registerReceiver(receiver, filter);
        enqueueWork(context, WebApiService.class, 225, intentForService);
    }

    public MainRequest getWeatherRequest() {
        return weatherRequest;
    }

    private void update (Object data) {
        if (data != null) {
            if (data instanceof WeatherRequest) {
                weatherRequest = (WeatherRequest) data;
            }
            else {
                Log.e(TAG, "UNKNOWN CLASS OF DATA");
            }

            observer.update(this, this);
        }
        else {
            Log.e(TAG, "PARCELABLE DATA IS NULL");
        }
    }

}
