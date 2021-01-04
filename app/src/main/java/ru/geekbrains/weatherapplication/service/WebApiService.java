package ru.geekbrains.weatherapplication.service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.weatherapplication.BuildConfig;
import ru.geekbrains.weatherapplication.data.dto.Weather;
import ru.geekbrains.weatherapplication.data.request.CurrentWeatherRequest;
import ru.geekbrains.weatherapplication.data.request.MainRequest;
import ru.geekbrains.weatherapplication.data.request.WeatherRequest;
import ru.geekbrains.weatherapplication.data.request.WeekWeatherRequest;

import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;

public class WebApiService extends JobIntentService {
    private static final String TAG = WebApiService.class.getSimpleName();

    public static final String WEATHER_REQUEST_MODE = "weather_request_mode";
    public static final String WEATHER_REQUEST_RESULT = "weather_request_result";
    public static final String GET_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?";
    public static final String GET_WEEK_WEATHER_URL = "https://api.openweathermap.org/data/2.5/onecall?";

    private int requestMode;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String requestUrl = buildRequestUrl(intent);
        try {
            final URL uri = new URL(requestUrl);

            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                if (DEBUG) {
                    Log.d(TAG, "Connection response code: : "+urlConnection.getResponseCode());
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = in.lines().collect(Collectors.joining("\n"));
                if (DEBUG) {
                    Log.d(TAG, "Weather result: "+result);
                }

                convertAndBroadcast(result);
                in.close();
            }
            catch (Exception e) {
                Log.e(TAG, "onHandleWork, request -> connection - failed", e);
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


        } catch (MalformedURLException e) {
            Log.e(TAG, "onHandleWork, cannot create URL", e);
            e.printStackTrace();
        }

    }

    private String buildRequestUrl(Intent intent) {
        StringBuilder requestUrl = new StringBuilder(GET_WEATHER_URL);
        requestUrl.append("lat=").append(intent.getStringExtra("lat"))
                .append("&lon=").append(intent.getStringExtra("lon"))
                .append("&exclude=");

        requestMode = intent.getIntExtra(WEATHER_REQUEST_MODE, 0);
        switch (requestMode) {
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

    private void convertAndBroadcast(String result) {
        Gson gson = new Gson();
        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
        sendBroadcast(weatherRequest);

    }

    private void sendBroadcast (WeatherRequest result) {
        Intent broadcastIntent = new Intent(WEATHER_REQUEST_RESULT);
        broadcastIntent.putExtra(WEATHER_REQUEST_RESULT, result);
        sendBroadcast(broadcastIntent);
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
