package ru.geekbrains.weatherapplication.data.request;

import java.util.ArrayList;

import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.dto.DailyWeather;
import ru.geekbrains.weatherapplication.data.dto.Main;
import ru.geekbrains.weatherapplication.data.dto.Weather;

public interface WeatherRequest extends MainRequest {
    Weather getFirstWeather();
    Main getMain();
    CurrentWeather getCurrent();
    ArrayList<DailyWeather> getDaily();
}
