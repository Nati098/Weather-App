package ru.geekbrains.weatherapplication.data.dto;

public class DailyWeather {

    private Temperature temp;
    private Weather[] weather;

    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }
}
