package ru.geekbrains.weatherapplication.service;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import ru.geekbrains.weatherapplication.BuildConfig;

public class WebApiService extends JobIntentService {
    public static final String GET_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?";
    public static final String GET_WEEK_WEATHER_URL = "https://api.openweathermap.org/data/2.5/onecall?";
    public static final String WEATHER_REQUEST_MODE = "weather_request_mode";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String requestUrl = buildRequestUrl(intent);

    }

    private String buildRequestUrl(Intent intent) {
        StringBuilder requestUrl = new StringBuilder(GET_WEATHER_URL);
        requestUrl.append("lat=").append(intent.getStringExtra("lat"))
                .append("&lon=").append(intent.getStringExtra("lon"))
                .append("&exclude=");

        int mode = intent.getIntExtra(WEATHER_REQUEST_MODE, 0);
        switch (mode) {
            case 1:
                requestUrl.append(Exclude.MINUTELY.description).append(",")
                        .append(Exclude.HOURLY.description).append(",")
                        .append(Exclude.ALERTS.description)
                        .append("&appid=").append(BuildConfig.WEATHER_API_KEY);
                break;
            default:
                requestUrl.append(Exclude.MINUTELY.description).append(",")
                        .append(Exclude.HOURLY.description).append(",")
                        .append(Exclude.DAILY.description).append(",")
                        .append(Exclude.ALERTS.description)
                        .append("&appid=").append(BuildConfig.WEATHER_API_KEY);
                break;
        }
        return requestUrl.toString();
    }


    private enum Exclude {
        CURRENT("current"),
        MINUTELY("minutely"),
        HOURLY("hourly"),
        DAILY("daily"),
        ALERTS("alerts");


        private final String description;

        Exclude(String description) {
            this.description = description;
        }

        public String toStr() {
            return description;
        }
    }
}
