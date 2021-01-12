package ru.geekbrains.weatherapplication.service;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.weatherapplication.data.request.CurrentWeatherRequest;
import ru.geekbrains.weatherapplication.data.request.WeekWeatherRequest;


public interface ApiService {

    @GET("weather?")
    Single<CurrentWeatherRequest> getCurrentWeather(
            @Query("q") String cityName,
            @Query("appid") String apiKey);

    @GET("onecall?")
    Single<WeekWeatherRequest> getWeekWeather(
            @Query("lat") Float lat,
            @Query("lon") Float lon,
            @Query("exclude") String exclude,
            @Query("appid") String apiKey);

}
