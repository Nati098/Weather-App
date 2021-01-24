package ru.geekbrains.weatherapplication.data;

import java.io.Serializable;
import java.util.List;

import ru.geekbrains.weatherapplication.item.OptionItem;

public class Parcel implements Serializable {
    public String currCityName;
    public String cityName;
    public List<OptionItem> options;
    public String icon;

    public Parcel(String cityName, List<OptionItem> options) {
        this.cityName = cityName;
        this.options = options;
    }

    public Parcel(String cityName, List<OptionItem> options, String icon) {
        this.cityName = cityName;
        this.options = options;
        this.icon = icon;
    }

    public Parcel(String currCityName, String cityName, List<OptionItem> options) {
        this.currCityName = currCityName;
        this.cityName = cityName;
        this.options = options;
    }
}