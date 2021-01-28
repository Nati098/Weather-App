package ru.geekbrains.weatherapplication.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static ru.geekbrains.weatherapplication.data.Constants.GET_WEEK_WEATHER_URL;


public class RetrofitClientImpl {
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
