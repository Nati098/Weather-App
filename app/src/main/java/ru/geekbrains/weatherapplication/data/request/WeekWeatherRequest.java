package ru.geekbrains.weatherapplication.data.request;

import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.dto.DailyWeather;


public class WeekWeatherRequest {

    private CurrentWeather current;
    private DailyWeather daily;

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public DailyWeather getDaily() {
        return daily;
    }

    public void setDaily(DailyWeather daily) {
        this.daily = daily;
    }
}
