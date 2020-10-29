package ru.geekbrains.weatherapplication.data;

public class WeatherItem {
    private int imgId;
    private int temp;
    private String timePoint;

    public WeatherItem(String timePoint, int imgId, int temp) {
        this.imgId = imgId;
        this.temp = temp;
        this.timePoint = timePoint;
    }

    public int getImgId() {
        return imgId;
    }

    public int getTemp() {
        return temp;
    }

    public String getTimePoint() {
        return timePoint;
    }
}
