package ru.geekbrains.weatherapplication.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class DailyWeather {

    @SerializedName("temp")
    @Expose
    private Temperature temp;

    @SerializedName("weather")
    @Expose
    private ArrayList<Weather> weather;


    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = (ArrayList<Weather>) weather.clone();
    }

}
