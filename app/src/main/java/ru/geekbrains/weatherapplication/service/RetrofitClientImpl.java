package ru.geekbrains.weatherapplication.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClientImpl {
    public static final String GET_WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String GET_WEEK_WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(GET_WEEK_WEATHER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
