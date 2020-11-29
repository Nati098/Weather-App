package ru.geekbrains.weatherapplication;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.weatherapplication.data.request.WeatherRequest;
import ru.geekbrains.weatherapplication.fragment.WeatherInfoFragment;

import static ru.geekbrains.weatherapplication.data.Constants.LoggerMode.DEBUG;

public class WebApi {
    private static final String TAG = WebApi.class.getSimpleName();

    private static final String GET_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?";


    private static void displayWeather(Fragment fragment, WeatherRequest weatherRequest) {
        if (fragment instanceof WeatherInfoFragment) {
            ((WeatherInfoFragment) fragment).updateView(weatherRequest);
        }
    }




}



