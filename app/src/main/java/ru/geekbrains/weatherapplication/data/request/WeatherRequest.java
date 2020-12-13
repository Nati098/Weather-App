package ru.geekbrains.weatherapplication.data.request;

import ru.geekbrains.weatherapplication.data.dto.Clouds;
import ru.geekbrains.weatherapplication.data.dto.Coordinates;
import ru.geekbrains.weatherapplication.data.dto.CurrentWeather;
import ru.geekbrains.weatherapplication.data.dto.Main;
import ru.geekbrains.weatherapplication.data.dto.Wind;

public class WeatherRequest {
    private Coordinates coord;
    private CurrentWeather[] weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private String name;

    public Coordinates getCoord() {
        return coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public CurrentWeather[] getWeather() {
        return weather;
    }

    public void setWeather(CurrentWeather[] weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
