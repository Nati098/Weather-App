package ru.geekbrains.weatherapplication.service;

import android.content.Context;
import android.content.Intent;

import ru.geekbrains.weatherapplication.data.dto.CityListItem;

import static ru.geekbrains.weatherapplication.service.WebApiService.WEATHER_REQUEST_MODE;


public class ApiDataParcer implements Runnable {

    private Context context;

    private CityListItem city;
    private int requestMode;

    @Override
    public void run() {
        Intent intentForService = new Intent(context, WebApiService.class);
        intentForService.putExtra(WEATHER_REQUEST_MODE, requestMode);
        intentForService.putExtra("lat", city.getCoord().getLat());
        intentForService.putExtra("lon", city.getCoord().getLon());
    }
}
