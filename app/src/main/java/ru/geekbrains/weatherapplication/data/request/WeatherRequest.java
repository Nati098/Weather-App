package ru.geekbrains.weatherapplication.data.request;

import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.dto.DailyWeather;
import ru.geekbrains.weatherapplication.data.dto.Main;
import ru.geekbrains.weatherapplication.data.dto.Weather;

public interface WeatherRequest extends MainRequest {
    Weather getFirstWeather();
    Main getMain();
    CurrentWeather getCurrent();
    DailyWeather[] getDaily();
}
