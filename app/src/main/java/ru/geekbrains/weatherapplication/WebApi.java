package ru.geekbrains.weatherapplication;

import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.weatherapplication.data.Constants;
import ru.geekbrains.weatherapplication.data.request.WeatherRequest;
import ru.geekbrains.weatherapplication.fragment.WeatherInfoFragment;

public class WebApi {
    private static final String TAG = "WebApi";

    public static final String API_KEY = "d58a8ddd18dd1ab39049edc96fac7f39";

    private static final String GET_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather? lat=55.75&lon=37.62&appid=";


    public static void getWeather(Fragment fragment, float lat, float lon) {
        StringBuilder requestUrl = new StringBuilder(GET_WEATHER_URL);

        requestUrl.append("lat=").append(lat)
                .append("&lon="+ lon)
                .append("&appid=").append(API_KEY);

        try {
            final URL uri = new URL(requestUrl.toString());
            final Handler handler = new Handler();

            new Thread(() -> {
                HttpsURLConnection urlConnection = null;

                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = in.lines().collect(Collectors.joining("\n"));

                    // data to model
                    Gson gson = new Gson();
                    final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);

                    // to main thread
                    handler.post(() -> displayWeather(fragment, weatherRequest));
                } catch (Exception e) {
                    Log.e(TAG, "getWeather request -> connection - failed", e);
                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }

            });


        } catch (MalformedURLException e) {
            if (Constants.LoggerMode.DEBUG) {
                Log.e(TAG, "getWeather request -> create uri - failed");
            }
            e.printStackTrace();
        }
    }

    private static void displayWeather(Fragment fragment, WeatherRequest weatherRequest) {
        if (fragment instanceof WeatherInfoFragment) {
            ((WeatherInfoFragment) fragment).updateView(weatherRequest);
        }
    }

}
