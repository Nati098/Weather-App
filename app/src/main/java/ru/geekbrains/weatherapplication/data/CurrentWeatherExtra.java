package ru.geekbrains.weatherapplication.data;


public class CurrentWeatherExtra {
    private int imgId;
    private String label;
    private String value;

    public CurrentWeatherExtra(int imgId, String label, String value) {
        this.imgId = imgId;
        this.label = label;
        this.value = value;
    }

    public int getImgId() {
        return imgId;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

}
