package ru.geekbrains.weatherapplication.item;


public class CurrentWeatherExtraItem {
    private int imgId;
    private String label;
    private String value;

    public CurrentWeatherExtraItem(int imgId, String label, String value) {
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
