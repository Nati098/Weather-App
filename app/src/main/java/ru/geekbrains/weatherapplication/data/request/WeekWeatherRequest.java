package ru.geekbrains.weatherapplication.data.request;

import java.util.ArrayList;

import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.dto.DailyWeather;
import ru.geekbrains.weatherapplication.data.dto.Main;
import ru.geekbrains.weatherapplication.data.dto.Weather;


public class WeekWeatherRequest implements WeatherRequest {

    private CurrentWeather current;
    private ArrayList<DailyWeather> daily;


    @Override
    public Weather getFirstWeather() {
        return current.getWeather().get(0);
    }

    @Override
    public Main getMain() {
        return null;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public ArrayList<DailyWeather> getDaily() {
        return daily;
    }

    public void setDaily(ArrayList<DailyWeather> daily) {
        this.daily = (ArrayList<DailyWeather>) daily.clone();
    }

}
